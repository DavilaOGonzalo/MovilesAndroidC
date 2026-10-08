package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.AvatarInicial
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.TarjetaSeccion
import com.saludplus.citas.ui.components.gradienteMarca

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    val usuario = Repositorio.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(gradienteMarca())
                .padding(horizontal = 20.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarInicial(nombre = usuario?.nombre ?: "?", tamano = 96.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = usuario?.nombre ?: "Sin usuario",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = usuario?.email ?: "Sin usuario",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Datos de la cuenta",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TarjetaSeccion(
                titulo = "Nombre",
                descripcion = usuario?.nombre ?: "Sin usuario",
                icono = Icons.Filled.Person
            )
            Spacer(modifier = Modifier.height(12.dp))
            TarjetaSeccion(
                titulo = "Email",
                descripcion = usuario?.email ?: "Sin usuario",
                icono = Icons.Filled.Email,
                colorIcono = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(28.dp))

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
}
