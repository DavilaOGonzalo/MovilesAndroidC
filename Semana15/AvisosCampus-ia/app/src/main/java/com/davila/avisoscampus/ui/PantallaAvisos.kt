package com.davila.avisoscampus.ui

import android.Manifest
import android.os.Build
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.davila.avisoscampus.model.PermisoUi
import com.davila.avisoscampus.notificacion.Notificador
import com.davila.avisoscampus.receiver.ModoAvionReceiver
import com.davila.avisoscampus.receiver.CargadorReceiver
import androidx.core.content.ContextCompat
import com.davila.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val escuchandoCargador by vm.escuchandoCargador.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto, receptor, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }
    DisposableEffect(escuchandoCargador) {
        if (escuchandoCargador) {
            val receptor = CargadorReceiver { conectado -> vm.alCambiarCargador(conectado) }
            ContextCompat.registerReceiver(
                contexto, receptor,
                IntentFilter().apply {
                    addAction(Intent.ACTION_POWER_CONNECTED)
                    addAction(Intent.ACTION_POWER_DISCONNECTED)
                },
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) vm.noSePudoMostrar()
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
                    if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
                    else if (Build.VERSION.SDK_INT >= 33) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else vm.permisoRespondido(true)
                }) {
                    Text("Permitir notificaciones")
                }
                if (mensaje != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Text(
                            mensaje.orEmpty(),
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Button(onClick = {
                        contexto.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                        })
                    }) { Text("Abrir ajustes") }
                }
            }
            Tarjeta("Avisos") {
                Button(onClick = vm::avisarAhora) {
                    Text("Mostrar aviso")
                }
            }
            Tarjeta("Recordatorio") {
                if (faltan != null) Text("Faltan ${faltan}s")
                Button(onClick = { vm.recordarEn() }, enabled = faltan == null) {
                    Text("Recordar en 10 s")
                }
                OutlinedButton(onClick = vm::cancelarRecordatorio, enabled = faltan != null) {
                    Text("Cancelar")
                }
            }
            Tarjeta("Receptor de anuncios") {
                Text("Escuchar el modo avión")
                Switch(checked = escuchando, onCheckedChange = vm::cambiarEscucha)
            }
            Tarjeta("Receptor del cargador") {
                Text("Escuchar el cargador")
                Switch(checked = escuchandoCargador, onCheckedChange = vm::cambiarEscuchaCargador)
            }
            HistorialAvisos(avisos)
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
