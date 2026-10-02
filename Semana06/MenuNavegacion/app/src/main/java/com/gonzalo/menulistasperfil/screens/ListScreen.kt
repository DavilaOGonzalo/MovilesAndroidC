package com.gonzalo.menulistasperfil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gonzalo.menulistasperfil.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {

    val items = (1..8).map {
        "Elemento número $it"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lista")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
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

                var expanded by remember {
                    mutableStateOf(false)
                }

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
                                    contentDescription = "Opciones"
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
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Favoritos"
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                    }
                                )

                                HorizontalDivider()

                                DropdownMenuItem(
                                    text = {
                                        Text("Compartir")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Compartir"
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                    }
                                )

                                HorizontalDivider()

                                DropdownMenuItem(
                                    text = {
                                        Text("Reportar")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Flag,
                                            contentDescription = "Reportar"
                                        )
                                    },
                                    onClick = {
                                        expanded = false
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