package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.components.TarjetaSeccion

@Composable
fun HomeScreen(onIrEspecialidades: () -> Unit) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Hola, ${usuario?.nombre ?: "Paciente"}",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "¿Cómo puedo ayudarte hoy?",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Especialidades destacadas",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(destacadas) { especialidad ->
                TarjetaEspecialidad(especialidad = especialidad)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Accesos rápidos",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        TarjetaSeccion(
            titulo = "Especialidades",
            descripcion = "Explora todas las especialidades disponibles",
            onClick = onIrEspecialidades
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaSeccion(
            titulo = "Médicos",
            descripcion = "Consulta a los médicos por especialidad"
        )
    }
}
