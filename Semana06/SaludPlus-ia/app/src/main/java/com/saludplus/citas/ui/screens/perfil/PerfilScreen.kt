package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.AvatarInicial
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.TarjetaSeccion

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    val usuario = Repositorio.usuarioActual
    val nombre = usuario?.nombre ?: "Gonzalo Dávila"
    val email = usuario?.email ?: "gonzalo@correo.com"
    val telefono = usuario?.telefono?.ifBlank { "+51 987 654 321" } ?: "+51 987 654 321"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarInicial(nombre = nombre, tamano = 64.dp)
            Spacer(modifier = Modifier.height(9.dp))
            Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    Icons.Filled.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(telefono, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TarjetaSeccion(
            titulo = "Mis datos",
            descripcion = "Nombre, correo y teléfono",
            icono = Icons.Filled.Person,
            flecha = true,
            colorIcono = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        TarjetaSeccion(
            titulo = "Mis citas",
            descripcion = "Consulta tus citas médicas",
            icono = Icons.Filled.DateRange,
            flecha = true,
            colorIcono = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        TarjetaSeccion(
            titulo = "Reportes médicos",
            descripcion = "Revisa tus resultados y reportes",
            icono = Icons.Filled.Info,
            flecha = true,
            colorIcono = ColorReporte
        )
        Spacer(modifier = Modifier.height(8.dp))
        TarjetaSeccion(
            titulo = "Notificaciones",
            descripcion = "Recordatorios y novedades",
            icono = Icons.Filled.Notifications,
            flecha = true,
            colorIcono = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(20.dp))
        BotonSecundario(
            texto = "Cerrar sesión",
            colorError = true,
            onClick = {
                Repositorio.cerrarSesion()
                onCerrarSesion()
            }
        )
    }
}

private val ColorReporte = androidx.compose.ui.graphics.Color(0xFFE53955)
