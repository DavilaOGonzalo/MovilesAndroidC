package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun gradienteMarca(): Brush {
    return Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
}

@Composable
fun gradienteExito(): Brush {
    return Brush.horizontalGradient(
        listOf(Color(0xFF0E7C86), Color(0xFF43A047))
    )
}

@Composable
fun gradienteAvatar(nombre: String): Brush {
    val base = ((nombre.hashCode() % 360) + 360) % 360
    val c1 = Color.hsv(base.toFloat(), 0.45f, 0.80f)
    val c2 = Color.hsv((base + 50).toFloat() % 360f, 0.50f, 0.62f)
    return Brush.linearGradient(listOf(c1, c2))
}

data class EstiloEspecialidad(
    val color: Color,
    val icono: ImageVector
)

@Composable
fun estiloEspecialidad(id: Int): EstiloEspecialidad {
    return when (id) {
        1 -> EstiloEspecialidad(Color(0xFFE53935), Icons.Filled.Favorite)
        2 -> EstiloEspecialidad(Color(0xFFFB8C00), Icons.Filled.Face)
        3 -> EstiloEspecialidad(Color(0xFF8E24AA), Icons.Filled.Star)
        4 -> EstiloEspecialidad(Color(0xFF6D4C41), Icons.Filled.Build)
        5 -> EstiloEspecialidad(Color(0xFF3949AB), Icons.Filled.Info)
        6 -> EstiloEspecialidad(Color(0xFF00897B), Icons.Filled.Search)
        else -> EstiloEspecialidad(MaterialTheme.colorScheme.primary, Icons.Filled.Star)
    }
}
