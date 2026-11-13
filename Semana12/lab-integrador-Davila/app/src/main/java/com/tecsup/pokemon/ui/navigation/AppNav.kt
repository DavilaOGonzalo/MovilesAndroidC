package com.tecsup.pokemon.ui.navigation

import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.admin.AdminScreen
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel
import com.tecsup.pokemon.ui.components.Cargando

private const val LISTA = "lista"
private const val FAVORITOS = "favoritos"
private const val ADMIN = "admin"
private const val DETALLE = "detalle/{id}"

@Composable
fun AppNav(container: AppContainer) {
    val loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory { LoginViewModel(container.authRepository) })
    val sesion by loginViewModel.sesion.collectAsStateWithLifecycle()
    val cargando by loginViewModel.cargando.collectAsStateWithLifecycle()

    when {
        cargando -> Cargando()
        sesion == null -> LoginScreen(loginViewModel)
        else -> PokemonNavigation(container, sesion!!, loginViewModel::cerrarSesion)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonNavigation(container: AppContainer, sesion: Sesion, cerrarSesion: () -> Unit) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route ?: LISTA
    val listaViewModel: ListaViewModel = viewModel(
        key = "lista-${sesion.uid}",
        factory = ViewModelFactory { ListaViewModel(container.pokemonRepository, container.notificador) },
    )
    val isAdmin = sesion.rol == Rol.ADMIN
    val showBottomBar = currentRoute != DETALLE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PokemonFire") },
                actions = {
                    Text(sesion.nombre)
                    Button(onClick = cerrarSesion) { Text("Salir") }
                },
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == LISTA,
                        onClick = { navController.navigate(LISTA) { launchSingleTop = true; popUpTo(LISTA) { saveState = true } } },
                        icon = { Text("🔥") },
                        label = { Text("Pokédex") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == FAVORITOS,
                        onClick = { navController.navigate(FAVORITOS) { launchSingleTop = true } },
                        icon = { Text("♥") },
                        label = { Text("Favoritos") },
                    )
                    if (isAdmin) {
                        NavigationBarItem(
                            selected = currentRoute == ADMIN,
                            onClick = { navController.navigate(ADMIN) { launchSingleTop = true } },
                            icon = { Text("⚙") },
                            label = { Text("Admin") },
                        )
                    }
                }
            }
        },
    ) { insets ->
        NavHost(
            navController = navController,
            startDestination = LISTA,
            modifier = Modifier.padding(insets),
        ) {
            composable(LISTA) {
                ListaScreen(listaViewModel) { id -> navController.navigate("detalle/${Uri.encode(id)}") }
            }
            composable(FAVORITOS) {
                val viewModel: FavoritosViewModel = viewModel(
                    factory = ViewModelFactory { FavoritosViewModel(container.pokemonRepository) },
                )
                FavoritosScreen(viewModel) { id -> navController.navigate("detalle/${Uri.encode(id)}") }
            }
            if (isAdmin) {
                composable(ADMIN) {
                    val viewModel: AdminViewModel = viewModel(
                        factory = ViewModelFactory { AdminViewModel(container.pokemonRepository, container.notificador) },
                    )
                    AdminScreen(viewModel)
                }
            }
            composable(
                route = DETALLE,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { backStackEntry ->
                val id = Uri.decode(backStackEntry.arguments?.getString("id").orEmpty())
                val viewModel: DetalleViewModel = viewModel(
                    key = "detalle-$id",
                    factory = ViewModelFactory { DetalleViewModel(container.pokemonRepository, id) },
                )
                DetalleScreen(viewModel)
            }
        }
    }
}
