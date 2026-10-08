package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoConVolver
import java.time.LocalDate

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onConfirmado: () -> Unit,
    onVolver: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val fechaLarga = runCatching { LocalDate.parse(fecha) }
        .getOrNull()
        ?.let { Repositorio.fechaLarga(it) }
        ?: fecha

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Confirmar cita", onVolver = onVolver)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Revisa los detalles antes de confirmar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                DetalleFila(titulo = "Especialidad", valor = especialidad?.nombre ?: "No encontrada")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                DetalleFila(titulo = "Médico", valor = medico?.nombre ?: "No encontrado")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                DetalleFila(titulo = "Fecha", valor = fechaLarga)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                DetalleFila(titulo = "Hora", valor = hora)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        BotonPrincipal(
            texto = "Confirmar cita",
            onClick = {
                val guardada = Repositorio.agendarCita(medicoId, fecha, hora)
                if (guardada) {
                    onConfirmado()
                }
            }
        )
    }
}

@Composable
private fun DetalleFila(titulo: String, valor: String) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
