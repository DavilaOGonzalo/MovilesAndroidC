package com.tecsup.pokemon.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Pokemon>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Pokemon>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repository.favoritos.collect { _estado.value = UiState.Exito(it) }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch { repository.alternarFavorito(id) }
    }
}
