package com.tecsup.pokemon.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Pokemon

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = pokemon.imagenUrl,
                contentDescription = "Imagen de ${pokemon.nombre}",
                modifier = Modifier.height(76.dp).weight(1f),
                contentScale = ContentScale.Fit,
            )
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    pokemon.nombre.replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(pokemon.tipos.ifEmpty { listOf("Tipo Fuego") }.joinToString(" · ").replaceFirstChar { it.titlecase() })
                pokemon.experienciaBase?.let { Text("EXP base: $it", style = MaterialTheme.typography.bodySmall) }
            }
            TextButton(onClick = onFavorite) {
                Text(if (pokemon.esFavorito) "♥" else "♡", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
