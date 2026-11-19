package com.tecsup.pokemon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PokemonColors = lightColorScheme(
    primary = FireOrange,
    onPrimary = Cream,
    secondary = Ember,
    tertiary = FlameYellow,
    background = Cream,
    surface = Cream,
    onSurface = Ink,
)

@Composable
fun PokemonFireTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PokemonColors, content = content)
}
