package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.TarjetaSeccion
import java.time.LocalDate

@Composable
fun MisCitasScreen() {
    val citas = Repositorio.citasDelUsuario()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tus citas médicas agendadas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (citas.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.DateRange,
                texto = "No tienes citas agendadas todavía"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(citas) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let {
                        Repositorio.obtenerEspecialidad(it.especialidadId)
                    }
                    val fechaLarga = runCatching { LocalDate.parse(cita.fecha) }
                        .getOrNull()
                        ?.let { Repositorio.fechaLarga(it) }
                        ?: cita.fecha
                    TarjetaSeccion(
                        titulo = "${especialidad?.nombre ?: "Especialidad"} - ${medico?.nombre ?: "Médico"}",
                        descripcion = "$fechaLarga · ${cita.hora}"
                    )
                }
            }
        }
    }
}
