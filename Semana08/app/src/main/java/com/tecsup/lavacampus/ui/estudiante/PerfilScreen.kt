package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import com.tecsup.lavacampus.viewmodel.PerfilViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import java.util.Locale

@Composable
fun PerfilScreen(
    usuario: Usuario,
    perfilVm: PerfilViewModel,
    sesionVm: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val activos by perfilVm.activos.collectAsStateWithLifecycle()
    val totales by perfilVm.totales.collectAsStateWithLifecycle()
    val kilos by perfilVm.kilosLavados.collectAsStateWithLifecycle()
    val oscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
    val avisos by sesionVm.notificaciones.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTopBar(title = "Perfil")
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(usuario.nombre, style = MaterialTheme.typography.titleLarge)
                Text(usuario.codigo, color = MaterialTheme.colorScheme.primary)
                Text(usuario.correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            EstadisticaPerfil(activos.toString(), "Activos")
            EstadisticaPerfil(totales.toString(), "Pedidos")
            EstadisticaPerfil(String.format(Locale.US, "%.1f kg", kilos), "Lavados")
        }
        HorizontalDivider()
        Text("Preferencias", style = MaterialTheme.typography.titleMedium)
        PreferenciaRow("Modo oscuro", oscuro, sesionVm::guardarModoOscuro)
        PreferenciaRow("Avisos de pedidos", avisos, sesionVm::guardarNotificaciones)
        LavaButton(text = "Cambiar de perfil", onClick = onCambiarPerfil)
        OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun EstadisticaPerfil(valor: String, etiqueta: String) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(valor, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(etiqueta, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun PreferenciaRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, modifier = Modifier.padding(vertical = 8.dp))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
