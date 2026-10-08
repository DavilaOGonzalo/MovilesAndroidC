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
            Color(0xFF1878F0),
            Color(0xFF2C86F3)
        )
    )
}

@Composable
fun gradienteEncabezado(): Brush {
    return Brush.verticalGradient(
        listOf(
            Color(0xFFEAF5FF),
            MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun gradienteExito(): Brush {
    return Brush.horizontalGradient(
        listOf(Color(0xFF2E9E5B), Color(0xFF43C06B))
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
        1 -> EstiloEspecialidad(Color(0xFF3A8DFF), Icons.Filled.Search)
        2 -> EstiloEspecialidad(Color(0xFF21B68A), Icons.Filled.Face)
        3 -> EstiloEspecialidad(Color(0xFFF44C65), Icons.Filled.Favorite)
        4 -> EstiloEspecialidad(Color(0xFFFF8C3A), Icons.Filled.Build)
        5 -> EstiloEspecialidad(Color(0xFFE83D8C), Icons.Filled.Favorite)
        6 -> EstiloEspecialidad(Color(0xFF5B73EE), Icons.Filled.Build)
        7 -> EstiloEspecialidad(Color(0xFF6654D9), Icons.Filled.Info)
        8 -> EstiloEspecialidad(Color(0xFF3979DD), Icons.Filled.Search)
        else -> EstiloEspecialidad(MaterialTheme.colorScheme.primary, Icons.Filled.Star)
    }
}

fun colorDisponibilidad(disponible: Boolean): Color {
    return if (disponible) Color(0xFF2E9E5B) else Color(0xFF9AA5B4)
}
