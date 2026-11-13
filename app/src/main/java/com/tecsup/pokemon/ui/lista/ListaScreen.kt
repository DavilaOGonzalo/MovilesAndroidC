package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.PokemonCard
import com.tecsup.pokemon.ui.components.VacioView

@Composable
fun ListaScreen(viewModel: ListaViewModel, abrirDetalle: (String) -> Unit) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        Text(
            "Catálogo tipo Fuego",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        )
        if (aviso != null) {
            Text(aviso.orEmpty(), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
        }
        when (val state = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(state.mensaje) { viewModel.sincronizar() }
            is UiState.Exito -> if (state.datos.isEmpty()) {
                VacioView("No hay Pokémon guardados. Conéctate a internet e inténtalo de nuevo.")
            } else {
                PokemonRows(state.datos, abrirDetalle, viewModel::alternarFavorito)
            }
        }
    }
}

@Composable
internal fun PokemonRows(
    pokemones: List<Pokemon>,
    abrirDetalle: (String) -> Unit,
    alternarFavorito: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(pokemones, key = Pokemon::id) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { abrirDetalle(pokemon.id) },
                onFavorite = { alternarFavorito(pokemon.id) },
            )
        }
    }
}
