package com.saludplus.citas.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

private val rutasPrincipales = listOf(
    Rutas.HOME,
    Rutas.CITAS,
    Rutas.RESULTADOS,
    Rutas.PERFIL
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (rutaActual in rutasPrincipales) {
                BarraNavegacionInferior(
                    rutaActual = rutaActual,
                    onSeleccionar = { ruta ->
                        navController.navigate(ruta) {
                            popUpTo(Rutas.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
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
                HomeScreen(
                    onIrEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) }
                )
            }
            composable(Rutas.ESPECIALIDADES) {
                EspecialidadesScreen(
                    onSeleccionar = { especialidadId ->
                        navController.navigate(Rutas.medicos(especialidadId))
                    }
                )
            }
            composable(
                route = Rutas.MEDICOS,
                arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
            ) { entry ->
                val especialidadId = entry.arguments?.getInt("especialidadId") ?: 0
                MedicosScreen(especialidadId = especialidadId)
            }
            composable(Rutas.CITAS) {
                MisCitasScreen()
            }
            composable(Rutas.RESULTADOS) {
                ResultadosScreen()
            }
            composable(Rutas.PERFIL) {
                PerfilScreen()
            }
        }
    }
}
