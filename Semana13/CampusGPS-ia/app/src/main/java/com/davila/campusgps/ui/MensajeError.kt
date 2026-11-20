package com.davila.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.LocationDisabled
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.davila.campusgps.model.TipoErrorUbicacion

@Composable
fun MensajeErrorUbicacion(
    mensaje: String,
    tipo: TipoErrorUbicacion,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val permiso = tipo == TipoErrorUbicacion.SIN_PERMISO
    val gpsApagado = tipo == TipoErrorUbicacion.GPS_APAGADO
    val titulo = when {
        permiso -> "Permiso necesario"
        gpsApagado -> "GPS desactivado"
        tipo == TipoErrorUbicacion.SIN_PLAY_SERVICES -> "Servicio de ubicación no disponible"
        else -> "No se pudo obtener la ubicación"
    }

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (permiso || gpsApagado) Icons.Rounded.LocationDisabled else Icons.Rounded.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(26.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall)
                Text(mensaje, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (permiso) {
                TextButton(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null),
                        )
                        context.startActivity(intent)
                    },
                ) {
                    Icon(Icons.Rounded.Settings, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Ajustes")
                }
            } else if (gpsApagado || tipo == TipoErrorUbicacion.OTRO) {
                Button(
                    onClick = onReintentar,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}
