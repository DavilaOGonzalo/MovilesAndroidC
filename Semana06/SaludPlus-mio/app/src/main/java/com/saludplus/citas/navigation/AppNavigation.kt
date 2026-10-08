package com.saludplus.citas.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(
                onSiguiente = { destino ->
                    navController.navigate(destino) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginCorrecto = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroCorrecto = { navController.popBackStack() },
                onIrTerminos = { navController.navigate(Rutas.TERMINOS) }
            )
        }
        composable(Rutas.TERMINOS) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }
        composable(Rutas.HOME) {
            HomeStub()
        }
    }
}

@Composable
private fun HomeStub() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Bienvenido a SaludPlus", style = MaterialTheme.typography.headlineSmall)
    }
}
