package com.tecsup.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding

data class LavaNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun LavaNavigationBar(
    admin: Boolean,
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = if (admin) {
        listOf(
            LavaNavItem("admin/panel", "Panel", Icons.Outlined.Dashboard),
            LavaNavItem("admin/pedidos", "Pedidos", Icons.AutoMirrored.Outlined.ListAlt),
            LavaNavItem("admin/servicios", "Servicios", Icons.Outlined.Build),
            LavaNavItem("admin/clientes", "Clientes", Icons.Outlined.People),
            LavaNavItem("admin/ajustes", "Ajustes", Icons.Outlined.Settings)
        )
    } else {
        listOf(
            LavaNavItem("estudiante/inicio", "Inicio", Icons.Outlined.Home),
            LavaNavItem("estudiante/pedidos", "Pedidos", Icons.AutoMirrored.Outlined.ListAlt),
            LavaNavItem("estudiante/tarifas", "Tarifas", Icons.Outlined.LocalOffer),
            LavaNavItem("estudiante/perfil", "Perfil", Icons.Outlined.Person)
        )
    }
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun LavaMainScaffold(
    admin: Boolean,
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            LavaNavigationBar(admin, selectedRoute, onNavigate)
        },
        content = content
    )
}
