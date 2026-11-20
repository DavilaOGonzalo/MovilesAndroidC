package com.davila.campusgps.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.luminance
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.Lugar
import androidx.compose.material3.MaterialTheme

fun iconoCategoria(categoria: CategoriaFiltro): ImageVector = when (categoria) {
    CategoriaFiltro.TODOS -> Icons.Rounded.Map
    CategoriaFiltro.ESTUDIO -> Icons.AutoMirrored.Rounded.MenuBook
    CategoriaFiltro.COMIDA -> Icons.Rounded.Restaurant
    CategoriaFiltro.EVENTOS -> Icons.Rounded.Event
    CategoriaFiltro.SERVICIOS -> Icons.Rounded.Build
}

@Composable
fun colorCategoria(lugar: Lugar): Color = when (lugar.categoria) {
    "Estudio" -> Color(0xFF3979D7)
    "Comida" -> Color(0xFFD87924)
    "Eventos" -> Color(0xFF8A59C7)
    "Servicios" -> Color(0xFF16836E)
    else -> MaterialTheme.colorScheme.primary
}

@Composable
fun Color.onColor(): Color = if (luminance() > 0.5f) Color(0xFF142536) else Color.White
