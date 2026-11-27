package com.davila.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.davila.avisoscampus.model.PermisoUi
import com.davila.avisoscampus.notificacion.Notificador
import com.davila.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, llamar a vm.noSePudoMostrar().
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("AvisosCampus") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                Text(when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir"
                    PermisoUi.Concedido -> "Permitido"
                    PermisoUi.Denegado -> "Denegado"
                }, color = when (permiso) {
                    PermisoUi.SinPedir -> Color.Gray
                    PermisoUi.Concedido -> Color(0xFF2E7D32)
                    PermisoUi.Denegado -> MaterialTheme.colorScheme.error
                })
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= 33) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else vm.permisoRespondido(true)
                }) {
                    Text("Permitir notificaciones")
                }
            }
            Tarjeta("Avisos") {
                Button(onClick = vm::avisarAhora) {
                    Text("Mostrar aviso")
                }
            }
            Tarjeta("Recordatorio") {
                Text("Sin recordatorio activo")
                Button(onClick = { /* TODO Paso 5: vm.recordarEn() */ }, enabled = false) {
                    Text("Recordar en 10 s")
                }
                Button(onClick = { /* TODO Paso 5: vm.cancelarRecordatorio() */ }, enabled = false) {
                    Text("Cancelar")
                }
            }
            Tarjeta("Receptor de anuncios") {
                // TODO Paso 6: conectar con escuchandoAvion y DisposableEffect.
                Text("Escuchar el modo avión")
                Switch(checked = false, onCheckedChange = null, enabled = false)
            }
            Tarjeta("Historial") {
                // TODO Paso 7: reemplazar con HistorialAvisos(vm.avisos.collectAsState().value).
                Text("Sin avisos todavía")
            }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}
