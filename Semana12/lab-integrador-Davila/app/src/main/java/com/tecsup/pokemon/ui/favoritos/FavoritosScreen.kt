package com.tecsup.pokemon.ui.favoritos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.VacioView
import com.tecsup.pokemon.ui.lista.PokemonRows

@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel, abrirDetalle: (String) -> Unit) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        Text("Mis favoritos", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(20.dp))
        when (val state = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> VacioView(state.mensaje)
            is UiState.Exito -> if (state.datos.isEmpty()) {
                VacioView("Todavía no guardaste favoritos. Toca el corazón de un Pokémon para agregarlo.")
            } else {
                PokemonRows(state.datos, abrirDetalle, viewModel::alternarFavorito)
            }
        }
    }
}
