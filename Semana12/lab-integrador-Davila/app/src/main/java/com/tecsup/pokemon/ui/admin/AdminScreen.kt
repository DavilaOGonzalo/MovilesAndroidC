package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.actualizar() }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text("Panel de administración", style = MaterialTheme.typography.headlineSmall)
        when (val state = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(state.mensaje, viewModel::actualizar)
            is UiState.Exito -> {
                Text("Pokémon guardados: ${state.datos.total}", style = MaterialTheme.typography.titleLarge)
                Text("Favoritos: ${state.datos.favoritos}", style = MaterialTheme.typography.titleLarge)
            }
        }
        Button(onClick = viewModel::sincronizar) { Text("Sincronizar PokéAPI") }
        OutlinedButton(onClick = viewModel::limpiarCache) { Text("Limpiar caché local") }
    }
}
