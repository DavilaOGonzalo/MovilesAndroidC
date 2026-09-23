package com.gonzalo.menulistasperfil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gonzalo.menulistasperfil.screens.DetailScreen
import com.gonzalo.menulistasperfil.screens.HomeScreen
import com.gonzalo.menulistasperfil.screens.ListScreen
import com.gonzalo.menulistasperfil.screens.LoginScreen
import com.gonzalo.menulistasperfil.screens.ProfileScreen
import com.gonzalo.menulistasperfil.screens.RecoverPasswordScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.RecoverPassword.route) {
            RecoverPasswordScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.List.route) {
            ListScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(name = "itemId") {
                    type = NavType.IntType
                },
            ),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(navController, itemId)
        }
    }
}