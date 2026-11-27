package com.davila.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AzulCampus = Color(0xFF1565C0)
val FondoCampus = Color(0xFFF6F8FC)

private val colores = lightColorScheme(
    primary = AzulCampus,
    onPrimary = Color.White,
    background = FondoCampus,
    onBackground = Color(0xFF1B1D21),
    surface = Color.White,
    onSurface = Color(0xFF1B1D21)
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colores, content = content)
}
