package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.saludplus.citas.navigation.Rutas

private data class DestinoInferior(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

private val destinosInferiores = listOf(
    DestinoInferior(Rutas.HOME, "Inicio", Icons.Filled.Home),
    DestinoInferior(Rutas.CITAS, "Citas", Icons.AutoMirrored.Filled.List),
    DestinoInferior(Rutas.RESULTADOS, "Resultados", Icons.Filled.Info),
    DestinoInferior(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun BarraNavegacionInferior(
    rutaActual: String?,
    onSeleccionar: (String) -> Unit
) {
    NavigationBar {
        destinosInferiores.forEach { destino ->
            NavigationBarItem(
                selected = rutaActual == destino.ruta,
                onClick = { onSeleccionar(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) }
            )
        }
    }
}
