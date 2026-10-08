package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.FotoPersona
import com.saludplus.citas.ui.theme.DisponibleVerde
import java.time.LocalDate

@Composable
fun MisCitasScreen() {
    var mostrarHistorial by remember { mutableStateOf(false) }
    val todas = Repositorio.citasDelUsuario()
    val citas = todas.filter { cita ->
        val fecha = runCatching { LocalDate.parse(cita.fecha) }.getOrNull()
        val futura = fecha?.isAfter(LocalDate.now().minusDays(1)) ?: true
        futura != mostrarHistorial
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TabCitas(
                texto = "Próximas",
                seleccionado = !mostrarHistorial,
                onClick = { mostrarHistorial = false },
                modifier = Modifier.weight(1f)
            )
            TabCitas(
                texto = "Historial",
                seleccionado = mostrarHistorial,
                onClick = { mostrarHistorial = true },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (citas.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.DateRange,
                texto = if (mostrarHistorial) {
                    "Aún no tienes citas en tu historial"
                } else {
                    "No tienes citas próximas"
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(citas) { cita -> CitaCard(cita) }
            }
        }
    }
}

@Composable
private fun TabCitas(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 9.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun CitaCard(cita: Cita) {
    val medico = Repositorio.obtenerMedico(cita.medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val fecha = runCatching { LocalDate.parse(cita.fecha) }.getOrNull()
    val fechaTexto = fecha?.let { Repositorio.fechaLarga(it) } ?: cita.fecha
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FotoPersona(url = medico?.fotoUrl.orEmpty(), tamano = 46.dp, anillo = false)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(medico?.nombre ?: "Médico", style = MaterialTheme.typography.titleSmall)
                    Text(
                        especialidad?.nombre ?: "Especialidad",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = DisponibleVerde.copy(alpha = 0.13f),
                    contentColor = DisponibleVerde
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Confirmada", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            CitaDato(Icons.Filled.DateRange, fechaTexto)
            CitaDato(Icons.Filled.DateRange, formatearHora(cita.hora))
            CitaDato(Icons.Filled.LocationOn, Repositorio.clinicaNombre)
        }
    }
}

@Composable
private fun CitaDato(icono: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(
        modifier = Modifier.padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(texto, style = MaterialTheme.typography.labelSmall)
    }
}

private fun formatearHora(hora: String): String {
    val partes = hora.split(":")
    val hora24 = partes.firstOrNull()?.toIntOrNull() ?: return hora
    val sufijo = if (hora24 < 12) "AM" else "PM"
    val hora12 = if (hora24 % 12 == 0) 12 else hora24 % 12
    return "%02d:%s %s".format(hora12, partes.getOrElse(1) { "00" }, sufijo)
}
