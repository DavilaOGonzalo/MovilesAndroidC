package com.gonzalo.gymtecsup_mio.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gonzalo.gymtecsup_mio.data.ClassCatalog
import com.gonzalo.gymtecsup_mio.screens.ClassDetailScreen
import com.gonzalo.gymtecsup_mio.screens.ConfirmationScreen
import com.gonzalo.gymtecsup_mio.screens.InicioScreen
import com.gonzalo.gymtecsup_mio.screens.MisReservasScreen
import com.gonzalo.gymtecsup_mio.screens.PerfilScreen
import com.gonzalo.gymtecsup_mio.screens.RutinasScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.HOME) {
                InicioScreen(
                    navController = navController,
                )
            }

            composable(Screen.RESERVAS) {
                MisReservasScreen()
            }

            composable(Screen.RUTINAS) {
                RutinasScreen()
            }

            composable(Screen.PERFIL) {
                PerfilScreen()
            }

            composable(
                route = Screen.CLASS_DETAIL,
                arguments = listOf(navArgument("classId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                val fitnessClass = ClassCatalog.classes.find { it.id == classId }
                if (fitnessClass != null) {
                    ClassDetailScreen(
                        fitnessClass = fitnessClass,
                        onBack = { navController.popBackStack() },
                        onReserve = {
                            navController.navigate(Screen.confirmation(classId))
                        },
                    )
                }
            }

            composable(
                route = Screen.CONFIRMATION,
                arguments = listOf(navArgument("classId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                ConfirmationScreen(
                    navController = navController,
                    classId = classId,
                )
            }
        }
    }
}