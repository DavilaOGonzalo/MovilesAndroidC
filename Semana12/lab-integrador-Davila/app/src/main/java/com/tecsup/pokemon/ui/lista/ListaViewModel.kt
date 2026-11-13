package com.tecsup.pokemon.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repository: PokemonRepository,
    private val notificador: Notificador,
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Pokemon>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Pokemon>>> = _estado.asStateFlow()
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repository.pokemones.collect { lista ->
                if (lista.isNotEmpty() || refrescoTerminado) _estado.value = UiState.Exito(lista)
            }
        }
        sincronizar(notificar = true)
    }

    fun sincronizar(notificar: Boolean = true) {
        viewModelScope.launch {
            _aviso.value = null
            repository.refrescar()
                .onSuccess { cantidad ->
                    refrescoTerminado = true
                    if (notificar) notificador.mostrar("Pokémon sincronizados", "Se guardaron $cantidad elementos")
                    _aviso.value = null
                    if (cantidad == 0) _estado.value = UiState.Exito(emptyList())
                }
                .onFailure { error ->
                    refrescoTerminado = true
                    val guardados = repository.cantidadGuardada()
                    if (guardados > 0) {
                        _aviso.value = "Sin conexión. Se muestran los datos guardados en este dispositivo."
                    } else {
                        _estado.value = UiState.Error(error.message ?: "No se pudo cargar el catálogo.")
                    }
                }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch { repository.alternarFavorito(id) }
    }
}
