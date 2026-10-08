package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TarjetaSeccion

@Composable
fun MisCitasScreen() {
    val citas = Repositorio.citasDelUsuario()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Mis Citas",
            style = MaterialTheme.typography.headlineSmall
        )
        if (citas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tienes citas agendadas",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(citas) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let {
                        Repositorio.obtenerEspecialidad(it.especialidadId)
                    }
                    TarjetaSeccion(
                        titulo = "${especialidad?.nombre ?: "Especialidad"} - ${medico?.nombre ?: "Médico"}",
                        descripcion = "Fecha: ${cita.fecha}  Hora: ${cita.hora}"
                    )
                }
            }
        }
    }
}
