package com.gonzalo.hospitaltecsup_ia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gonzalo.hospitaltecsup_ia.navigation.Screen

// Conjunto de destinos del menú lateral.
private data class DrawerDestination(
    val label: String,
    val route: String,
)

private val drawerDestinations = listOf(
    DrawerDestination("Inicio", Screen.Home.route),
    DrawerDestination("Mis citas", Screen.MisCitas.route),
    DrawerDestination("Historial médico", Screen.HistorialMedico.route),
)

// Contenido del NavigationDrawer compartido por las pantallas.
@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    currentRoute: String?,
    onNavigateTo: (String) -> Unit,
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        DrawerHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 28.dp, bottom = 8.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            drawerDestinations.forEach { destination ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = destination.label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    },
                    selected = currentRoute == destination.route,
                    onClick = { onNavigateTo(destination.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Clínica Salud+",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                    ),
                ),
            )
            .padding(horizontal = 24.dp, vertical = 40.dp),
    ) {
        Text(
            text = "Clínica Salud+",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tu centro médico de confianza",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f),
        )
    }
}