package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lavacampus.LavaApp
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.AjustesScreen
import com.tecsup.lavacampus.ui.administrador.BandejaScreen
import com.tecsup.lavacampus.ui.administrador.ClientesScreen
import com.tecsup.lavacampus.ui.administrador.FormularioServicioScreen
import com.tecsup.lavacampus.ui.administrador.GestionPedidoScreen
import com.tecsup.lavacampus.ui.administrador.PanelScreen
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.LavaMainScaffold
import com.tecsup.lavacampus.ui.estudiante.DetallePedidoScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.MisPedidosScreen
import com.tecsup.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.tecsup.lavacampus.ui.estudiante.PerfilScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.TarifasScreen
import com.tecsup.lavacampus.viewmodel.BandejaViewModel
import com.tecsup.lavacampus.viewmodel.ClientesViewModel
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import com.tecsup.lavacampus.viewmodel.FormularioServicioViewModel
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import com.tecsup.lavacampus.viewmodel.InicioViewModel
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import com.tecsup.lavacampus.viewmodel.PerfilViewModel
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun NavLava(preferencias: PreferenciasStore) {
    val context = LocalContext.current
    val app = context.applicationContext as LavaApp
    val navController = rememberNavController()
    val sesionVm: SesionViewModel = viewModel(
        factory = LavaViewModelFactory { SesionViewModel(app.repository, preferencias) }
    )
    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val perfilGuardado by preferencias.perfilActivo.collectAsStateWithLifecycle(initialValue = null)

    fun navegarPrincipal(rol: String) {
        val destino = if (rol == Rol.ADMIN.name) Rutas.PANEL else Rutas.INICIO
        navController.navigate(destino) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navegarLogin() {
        sesionVm.cerrarSesion()
        navController.navigate(Rutas.LOGIN) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen()
            LaunchedEffect(perfilGuardado) {
                if (perfilGuardado == null) return@LaunchedEffect
                delay(700)
                if (perfilGuardado.isNullOrBlank()) {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                } else {
                    navegarPrincipal(perfilGuardado.orEmpty())
                }
            }
        }
        composable(Rutas.LOGIN) {
            LoginScreen(sesionVm, onIngreso = ::navegarPrincipal)
        }

        composable(Rutas.INICIO) {
            val actual = usuario
            if (actual == null) Cargando() else {
                val vm: InicioViewModel = viewModel(factory = LavaViewModelFactory { InicioViewModel(app.repository, actual) })
                LavaMainScaffold(false, Rutas.INICIO, navController::navigate) { padding ->
                    InicioScreen(
                        vm = vm,
                        usuario = actual,
                        onNuevoPedido = { navController.navigate(Rutas.nuevoPedido()) },
                        onPedidos = { navController.navigate(Rutas.MIS_PEDIDOS) },
                        onTarifas = { navController.navigate(Rutas.TARIFAS) },
                        onDetalle = { navController.navigate(Rutas.detalle(it)) },
                        contentModifier = Modifier.padding(padding)
                    )
                }
            }
        }
        composable(Rutas.MIS_PEDIDOS) {
            val actual = usuario
            if (actual == null) Cargando() else {
                val vm: MisPedidosViewModel = viewModel(factory = LavaViewModelFactory { MisPedidosViewModel(app.repository, actual) })
                LavaMainScaffold(false, Rutas.MIS_PEDIDOS, navController::navigate) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        MisPedidosScreen(
                            vm = vm,
                            onBack = { navController.popBackStack() },
                            onDetalle = { navController.navigate(Rutas.detalle(it)) },
                            onNuevoPedido = { navController.navigate(Rutas.nuevoPedido()) },
                            onRepetir = { navController.navigate(Rutas.nuevoPedido(it)) }
                        )
                    }
                }
            }
        }
        composable(Rutas.TARIFAS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(app.repository) })
            LavaMainScaffold(false, Rutas.TARIFAS, navController::navigate) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) { TarifasScreen(vm, onBack = { navController.popBackStack() }) }
            }
        }
        composable(Rutas.PERFIL) {
            val actual = usuario
            if (actual == null) Cargando() else {
                val vm: PerfilViewModel = viewModel(factory = LavaViewModelFactory { PerfilViewModel(app.repository, actual) })
                LavaMainScaffold(false, Rutas.PERFIL, navController::navigate) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        PerfilScreen(actual, vm, sesionVm, ::navegarLogin, ::navegarLogin)
                    }
                }
            }
        }
        composable(
            route = Rutas.NUEVO_PEDIDO,
            arguments = listOf(navArgument("repeatId") { type = NavType.IntType; defaultValue = -1 })
        ) { entry ->
            val actual = usuario
            if (actual == null) Cargando() else {
                val repeatId = entry.arguments?.getInt("repeatId") ?: -1
                val vm: NuevoPedidoViewModel = viewModel(factory = LavaViewModelFactory { NuevoPedidoViewModel(app.repository, actual.id, repeatId) })
                val recibir by sesionVm.recibirPedidos.collectAsStateWithLifecycle()
                NuevoPedidoScreen(vm, recibir, onBack = { navController.popBackStack() }, onConfirmed = {
                    navController.navigate(Rutas.MIS_PEDIDOS) {
                        popUpTo(Rutas.INICIO)
                        launchSingleTop = true
                    }
                })
            }
        }
        composable(Rutas.DETALLE, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            val vm: DetallePedidoViewModel = viewModel(factory = LavaViewModelFactory { DetallePedidoViewModel(app.repository, id) })
            DetallePedidoScreen(vm, onBack = { navController.popBackStack() })
        }

        composable(Rutas.PANEL) {
            val vm: PanelViewModel = viewModel(factory = LavaViewModelFactory { PanelViewModel(app.repository) })
            LavaMainScaffold(true, Rutas.PANEL, navController::navigate) { padding ->
                PanelScreen(vm, onVerPedidos = { navController.navigate(Rutas.BANDEJA) }, contentModifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.BANDEJA) {
            val vm: BandejaViewModel = viewModel(factory = LavaViewModelFactory { BandejaViewModel(app.repository) })
            LavaMainScaffold(true, Rutas.BANDEJA, navController::navigate) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) { BandejaScreen(vm, { navController.popBackStack() }, { navController.navigate(Rutas.gestion(it)) }) }
            }
        }
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(app.repository) })
            LavaMainScaffold(true, Rutas.SERVICIOS, navController::navigate) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) {
                    ServiciosScreen(vm, { navController.navigate(Rutas.formularioServicio()) }, { navController.navigate(Rutas.formularioServicio(it)) }, { navController.popBackStack() })
                }
            }
        }
        composable(Rutas.CLIENTES) {
            val vm: ClientesViewModel = viewModel(factory = LavaViewModelFactory { ClientesViewModel(app.repository) })
            LavaMainScaffold(true, Rutas.CLIENTES, navController::navigate) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) { ClientesScreen(vm, { navController.popBackStack() }) }
            }
        }
        composable(Rutas.AJUSTES) {
            val actual = usuario
            if (actual == null) Cargando() else {
                LavaMainScaffold(true, Rutas.AJUSTES, navController::navigate) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) { AjustesScreen(actual, sesionVm, ::navegarLogin, ::navegarLogin) }
                }
            }
        }
        composable(
            Rutas.FORMULARIO_SERVICIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            val vm: FormularioServicioViewModel = viewModel(factory = LavaViewModelFactory { FormularioServicioViewModel(app.repository, id) })
            FormularioServicioScreen(vm, { navController.popBackStack() }, { navController.popBackStack() })
        }
        composable(Rutas.GESTION, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            val vm: GestionPedidoViewModel = viewModel(factory = LavaViewModelFactory { GestionPedidoViewModel(app.repository, id) })
            GestionPedidoScreen(vm, { navController.popBackStack() })
        }
    }
}

@Composable
private fun Cargando() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}
