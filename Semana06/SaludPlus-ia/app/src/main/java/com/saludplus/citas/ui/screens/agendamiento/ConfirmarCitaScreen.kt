package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.FotoPersona
import java.time.LocalDate

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    motivo: String = "Control médico",
    lugar: String = "${Repositorio.clinicaNombre} - Sede Principal",
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
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        EncabezadoConVolver(titulo = "Confirmación de cita", onVolver = onVolver)
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(58.dp).align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Filled.DateRange, contentDescription = null, modifier = Modifier.padding(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Revisa los datos de tu cita",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FotoPersona(url = medico?.fotoUrl.orEmpty(), tamano = 44.dp, anillo = false)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(medico?.nombre ?: "Médico", style = MaterialTheme.typography.titleSmall)
                        Text(
                            especialidad?.nombre ?: "Especialidad",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                DetalleFila(Icons.Filled.DateRange, "Fecha", fechaLarga)
                DetalleFila(Icons.Filled.Star, "Hora", formatearHora(hora))
                DetalleFila(Icons.Filled.Info, "Motivo", motivo)
                DetalleFila(Icons.Filled.Build, "Lugar", lugar)
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        BotonGradiente(
            texto = "Confirmar cita",
            onClick = {
                if (Repositorio.agendarCita(medicoId, fecha, hora)) onConfirmado()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DetalleFila(icono: ImageVector, titulo: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(9.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(icono, contentDescription = null, modifier = Modifier.padding(7.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
        }
    }
}

private fun formatearHora(hora: String): String {
    val partes = hora.split(":")
    val hora24 = partes.firstOrNull()?.toIntOrNull() ?: return hora
    val sufijo = if (hora24 < 12) "AM" else "PM"
    val hora12 = if (hora24 % 12 == 0) 12 else hora24 % 12
    return "%02d:%s %s".format(hora12, partes.getOrElse(1) { "00" }, sufijo)
}
