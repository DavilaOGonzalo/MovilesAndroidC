package com.tecsup.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun LavaTheme(
    rol: String? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val admin = rol == "ADMIN"
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = if (admin) Violeta else Turquesa,
            secondary = if (admin) VioletaContainer else TurquesaContainer,
            background = if (admin) VioletaFondo.copy(alpha = 0.12f) else TurquesaFondo.copy(alpha = 0.12f),
            surface = if (admin) VioletaFondo.copy(alpha = 0.18f) else TurquesaFondo.copy(alpha = 0.18f)
        )
    } else {
        lightColorScheme(
            primary = if (admin) Violeta else Turquesa,
            onPrimary = androidx.compose.ui.graphics.Color.White,
            primaryContainer = if (admin) VioletaContainer else TurquesaContainer,
            background = if (admin) VioletaFondo else TurquesaFondo,
            surface = androidx.compose.ui.graphics.Color.White
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = LavaTypography,
        content = content
    )
}
