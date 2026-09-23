package com.gonzalo.gymtecsup_mio.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGray
import com.gonzalo.gymtecsup_mio.ui.theme.WhiteSurface

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(route = Screen.HOME, label = "Inicio", icon = Icons.Outlined.Home),
    BottomNavItem(route = Screen.RESERVAS, label = "Reservas", icon = Icons.Outlined.CalendarMonth),
    BottomNavItem(route = Screen.RUTINAS, label = "Rutinas", icon = Icons.Outlined.FitnessCenter),
    BottomNavItem(route = Screen.PERFIL, label = "Perfil", icon = Icons.Outlined.Person),
)

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    androidx.compose.foundation.layout.Column {
        HorizontalDivider(color = LightGray, thickness = 1.dp)

        NavigationBar(
            containerColor = WhiteSurface,
            tonalElevation = 0.dp,
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreen,
                        selectedTextColor = DarkGreen,
                        unselectedIconColor = InkGray,
                        unselectedTextColor = InkGray,
                        indicatorColor = Color.Transparent,
                    ),
                    alwaysShowLabel = true,
                )
            }
        }
    }
}