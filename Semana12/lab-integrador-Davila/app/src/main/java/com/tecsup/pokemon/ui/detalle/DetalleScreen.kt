package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@Composable
fun DetalleScreen(viewModel: DetalleViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    when (val state = estado) {
        UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorView(state.mensaje, viewModel::cargar)
        is UiState.Exito -> {
            val pokemon = state.datos
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AsyncImage(
                    model = pokemon.imagenUrl,
                    contentDescription = "Imagen de ${pokemon.nombre}",
                    modifier = Modifier.weight(1f),
                    contentScale = ContentScale.Fit,
                )
                Text(pokemon.nombre.replaceFirstChar { it.titlecase() }, style = MaterialTheme.typography.headlineMedium)
                Text("Tipos: ${pokemon.tipos.joinToString(" · ").ifBlank { "Fuego" }}")
                Text("Altura: ${pokemon.altura?.let { "${it / 10.0} m" } ?: "—"}")
                Text("Peso: ${pokemon.peso?.let { "${it / 10.0} kg" } ?: "—"}")
                Text("Experiencia base: ${pokemon.experienciaBase ?: "—"}")
                Button(onClick = viewModel::alternarFavorito) {
                    Text(if (pokemon.esFavorito) "Quitar de favoritos" else "Guardar en favoritos")
                }
            }
        }
    }
}
