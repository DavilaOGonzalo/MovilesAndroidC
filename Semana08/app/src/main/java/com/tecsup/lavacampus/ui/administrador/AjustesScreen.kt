package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    usuario: Usuario,
    vm: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val avisos by vm.notificaciones.collectAsStateWithLifecycle()
    val recibir by vm.recibirPedidos.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTopBar(title = "Ajustes")
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(usuario.nombre, style = MaterialTheme.typography.titleLarge)
                Text("Administrador", color = MaterialTheme.colorScheme.primary)
                Text(usuario.correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        LavaButton(text = "Cambiar de perfil", onClick = onCambiarPerfil)
        Text("Preferencias", style = MaterialTheme.typography.titleMedium)
        AjusteSwitch("Modo oscuro", oscuro, vm::guardarModoOscuro)
        AjusteSwitch("Notificaciones", avisos, vm::guardarNotificaciones)
        AjusteSwitch("Recibir pedidos", recibir, vm::guardarRecibirPedidos)
        Text(
            if (recibir) "La lavandería está recibiendo pedidos" else "Los estudiantes no podrán crear pedidos",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
    }
}

@Composable
private fun AjusteSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, modifier = Modifier.padding(vertical = 8.dp))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
