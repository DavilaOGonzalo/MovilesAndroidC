package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saludplus.citas.navigation.Rutas

private data class DestinoInferior(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector,
    val color: Color
)

private val destinosInferiores = listOf(
    DestinoInferior(Rutas.HOME, "Inicio", Icons.Filled.Home, Color(0xFF1E6BD6)),
    DestinoInferior(Rutas.CITAS, "Citas", Icons.AutoMirrored.Filled.List, Color(0xFF00A8C6)),
    DestinoInferior(Rutas.RESULTADOS, "Resultados", Icons.Filled.Info, Color(0xFF8E24AA)),
    DestinoInferior(Rutas.PERFIL, "Perfil", Icons.Filled.Person, Color(0xFF2E9E5B))
)

@Composable
fun BarraNavegacionInferior(
    rutaActual: String?,
    onSeleccionar: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        destinosInferiores.forEach { destino ->
            val seleccionado = rutaActual == destino.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { onSeleccionar(destino.ruta) },
                icon = {
                    Icon(
                        imageVector = destino.icono,
                        contentDescription = destino.etiqueta,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = destino.color,
                    selectedTextColor = destino.color,
                    indicatorColor = destino.color.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
