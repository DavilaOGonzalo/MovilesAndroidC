package com.davila.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Column(modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(mensaje, color = MaterialTheme.colorScheme.error)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (tipo == TipoErrorUbicacion.SIN_PERMISO) {
                Button(onClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    )
                    context.startActivity(intent)
                }) {
                    Text("Abrir ajustes")
                }
            }
            if (tipo == TipoErrorUbicacion.GPS_APAGADO || tipo == TipoErrorUbicacion.OTRO) {
                Button(onClick = onReintentar) { Text("Reintentar") }
            }
        }
    }
}
