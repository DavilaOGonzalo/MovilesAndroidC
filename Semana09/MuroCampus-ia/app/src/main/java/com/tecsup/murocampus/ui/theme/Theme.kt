package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresMuroCampus = lightColorScheme(
    primary = Color(0xFF176398),
    onPrimary = Color.White,
    background = Color(0xFFF8FAFD),
    surface = Color(0xFFF8FAFD),
    surfaceVariant = Color(0xFFE6EDF5),
    secondaryContainer = Color(0xFFFFE3A1),
    onSecondaryContainer = Color(0xFF392B00),
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresMuroCampus, content = content)
}
