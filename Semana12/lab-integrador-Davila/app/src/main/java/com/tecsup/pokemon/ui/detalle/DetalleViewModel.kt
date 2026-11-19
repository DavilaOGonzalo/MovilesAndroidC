package com.tecsup.pokemon.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(private val repository: PokemonRepository, private val id: String) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Pokemon>>(UiState.Cargando)
    val estado: StateFlow<UiState<Pokemon>> = _estado.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repository.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "No se pudo cargar el detalle.") }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            repository.alternarFavorito(id)
            cargar()
        }
    }
}
