package com.tecsup.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState
import com.tecsup.murocampus.viewmodel.postsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = postsViewModelFactory {
                    PostsViewModel(PostRepositoryRemote(RetrofitClient.api))
                })
                val idSeleccionado by vm.idSeleccionado.collectAsStateWithLifecycle()
                val estadosPantallas = rememberSaveableStateHolder()
                val snackbar = remember { SnackbarHostState() }
                val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
                val avisoEliminacion = stringResource(R.string.publicacion_eliminada)
                LaunchedEffect(eliminacion) {
                    if (eliminacion is UiState.Exito) {
                        snackbar.showSnackbar(avisoEliminacion)
                        vm.consumirAvisoEliminacion()
                    }
                }
                var creando by rememberSaveable { mutableStateOf(false) }
                BackHandler(enabled = idSeleccionado != null, onBack = vm::cerrarDetalle)
                BackHandler(enabled = creando) {
                    vm.limpiarPublicacion()
                    creando = false
                }
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbar) },
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                        )
                    },
                    floatingActionButton = {
                        if (!creando && idSeleccionado == null) {
                            val etiqueta = stringResource(R.string.crear_publicacion)
                            FloatingActionButton(
                                onClick = { creando = true },
                                modifier = Modifier.semantics { contentDescription = etiqueta },
                            ) {
                                Text("+", style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    },
                ) { padding ->
                    val modifier = Modifier.padding(padding)
                    when {
                        creando -> PantallaCrear(vm, modifier)
                        idSeleccionado != null -> PantallaDetalle(vm, modifier)
                        else -> estadosPantallas.SaveableStateProvider("lista") {
                            PantallaLista(vm, modifier, alAbrir = vm::abrir)
                        }
                    }
                }
            }
        }
    }
}
