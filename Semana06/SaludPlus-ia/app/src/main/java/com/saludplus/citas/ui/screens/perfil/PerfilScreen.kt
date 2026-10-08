package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.TarjetaSeccion

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    val usuario = Repositorio.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        TarjetaSeccion(
            titulo = "Nombre",
            descripcion = usuario?.nombre ?: "Sin usuario"
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaSeccion(
            titulo = "Email",
            descripcion = usuario?.email ?: "Sin usuario"
        )
        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Cerrar sesión",
            onClick = {
                Repositorio.cerrarSesion()
                onCerrarSesion()
            }
        )
    }
}
