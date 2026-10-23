package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import java.util.Locale

@Composable
fun ServiciosScreen(
    vm: ServiciosViewModel,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    onBack: () -> Unit
) {
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    var porEliminar by remember { mutableStateOf<Servicio?>(null) }
    Scaffold(
        topBar = { AppTopBar(title = "Servicios y tarifas", onBack = onBack) },
        floatingActionButton = { FloatingActionButton(onClick = onNuevo) { Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio") } }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios, key = { it.id }) { servicio ->
                androidx.compose.material3.Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(servicio.icono)
                        Column(Modifier.weight(1f).padding(start = 10.dp)) {
                            Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(String.format(Locale.US, "S/ %.2f ${servicio.textoPrecio()} · ${servicio.horasEntrega} h", servicio.precio), color = MaterialTheme.colorScheme.primary)
                            Text(if (servicio.activo) "Activo" else "Inactivo", style = MaterialTheme.typography.labelSmall)
                        }
                        Switch(checked = servicio.activo, onCheckedChange = { vm.alternar(servicio) })
                        IconButton(onClick = { onEditar(servicio.id) }) { Icon(Icons.Outlined.Edit, contentDescription = "Editar") }
                        IconButton(onClick = { porEliminar = servicio }) { Icon(Icons.Outlined.DeleteOutline, contentDescription = "Eliminar") }
                    }
                }
            }
        }
    }
    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("No se puede eliminar") },
            text = { Text(mensaje.orEmpty()) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
    porEliminar?.let { servicio ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar ${servicio.nombre}?") },
            text = { Text("Si ya tiene pedidos, solo podrás desactivarlo.") },
            confirmButton = {
                TextButton(onClick = { vm.eliminar(servicio); porEliminar = null }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Volver") } }
        )
    }
}
