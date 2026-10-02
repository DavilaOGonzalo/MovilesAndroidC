package com.gonzalo.menulistasperfil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gonzalo.menulistasperfil.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {

    val items = (1..8).map { "Elemento número $it" }

    var expanded by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    // Ruta actual
    val currentRoute =
        navController.currentBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet {

                Text(
                    text = "Menú",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    )
                )

                // INICIO
                NavigationDrawerItem(
                    label = {
                        Text("Inicio")
                    },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Home.route)
                    },
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )

                // MIS PEDIDOS
                NavigationDrawerItem(
                    label = {
                        Text("Mis pedidos")
                    },
                    selected = currentRoute == Screen.List.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.List.route)
                    },
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )

                // FAVORITOS
                NavigationDrawerItem(
                    label = {
                        Text("Favoritos")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )

                // PERFIL
                NavigationDrawerItem(
                    label = {
                        Text("Perfil")
                    },
                    selected = currentRoute == Screen.Profile.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Profile.route)
                    },
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Lista")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { padding ->

            LazyColumn(
                contentPadding = padding
            ) {

                items(items.size) { index ->

                    ListItem(
                        headlineContent = {
                            Text(items[index])
                        },

                        supportingContent = {
                            Text("Toca para ver el detalle")
                        },

                        trailingContent = {

                            Box {

                                IconButton(
                                    onClick = {
                                        expanded = !expanded
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Más opciones"
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = {
                                        expanded = false
                                    }
                                ) {

                                    DropdownMenuItem(
                                        text = {
                                            Text("Favoritos")
                                        },
                                        onClick = {
                                            expanded = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    HorizontalDivider()

                                    DropdownMenuItem(
                                        text = {
                                            Text("Compartir")
                                        },
                                        onClick = {
                                            expanded = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Share,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    HorizontalDivider()

                                    DropdownMenuItem(
                                        text = {
                                            Text("Reportar")
                                        },
                                        onClick = {
                                            expanded = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Flag,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                            }
                        },

                        modifier = Modifier.clickable {
                            navController.navigate(
                                Screen.Detail.createRoute(index + 1)
                            )
                        }
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}