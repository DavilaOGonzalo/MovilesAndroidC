package com.gonzalo.gymtecsup_mio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gonzalo.gymtecsup_mio.data.ClassCatalog
import com.gonzalo.gymtecsup_mio.screens.ClassDetailScreen
import com.gonzalo.gymtecsup_mio.screens.InicioScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME,
    ) {
        composable(Screen.HOME) {
            InicioScreen(
                navController = navController,
            )
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
                )
            }
        }
    }
}