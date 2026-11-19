package com.tecsup.pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminStats(val total: Int, val favoritos: Int)

class AdminViewModel(
    private val repository: PokemonRepository,
    private val notificador: Notificador,
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<AdminStats>>(UiState.Cargando)
    val estado: StateFlow<UiState<AdminStats>> = _estado.asStateFlow()

    fun actualizar() {
        viewModelScope.launch {
            _estado.value = UiState.Exito(
                AdminStats(repository.cantidadGuardada(), repository.cantidadFavoritos()),
            )
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repository.limpiarCache()
            actualizar()
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            repository.refrescar().fold(
                onSuccess = { cantidad ->
                    notificador.mostrar("Pokémon sincronizados", "Se guardaron $cantidad elementos")
                    _estado.value = UiState.Exito(
                        AdminStats(repository.cantidadGuardada(), repository.cantidadFavoritos()),
                    )
                },
                onFailure = { _estado.value = UiState.Error(it.message ?: "No se pudo sincronizar.") },
            )
        }
    }
}
