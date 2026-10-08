package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.FotoPersona
import com.saludplus.citas.ui.theme.RatingAmber
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (Int, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var semanaBase by remember { mutableStateOf(Repositorio.semanaActual()) }
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val dias = Repositorio.diasHabilesDeLaSemana(semanaBase)
    val horariosDisponibles = fechaSeleccionada
        ?.let { Repositorio.horariosDisponibles(medicoId, it.toString()) }
        ?: emptyList()
    val puedeRetroceder = semanaBase.isAfter(Repositorio.semanaActual())

    fun cambiarSemana(nueva: LocalDate) {
        semanaBase = nueva
        if (fechaSeleccionada !in Repositorio.diasHabilesDeLaSemana(nueva)) {
            fechaSeleccionada = null
            horaSeleccionada = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        EncabezadoConVolver(titulo = "Agendar cita", onVolver = onVolver)
        Spacer(modifier = Modifier.height(12.dp))
        if (medico != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FotoPersona(url = medico.fotoUrl, tamano = 48.dp, anillo = false)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(medico.nombre, style = MaterialTheme.typography.titleSmall)
                        Text(
                            especialidad?.nombre ?: "Especialidad",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Selecciona una fecha",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { cambiarSemana(semanaBase.minusWeeks(1)) },
                enabled = puedeRetroceder
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Semana anterior",
                    tint = if (puedeRetroceder) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
            Text(
                text = Repositorio.nombreMesYAnio(semanaBase),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { cambiarSemana(semanaBase.plusWeeks(1)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Semana siguiente",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dias.forEach { fecha ->
                val partes = Repositorio.formatearFecha(fecha).split(" ")
                DiaCita(
                    dia = partes.getOrElse(0) { "" },
                    numero = partes.getOrElse(1) { "" }.substringBefore("/"),
                    seleccionado = fecha == fechaSeleccionada,
                    onClick = {
                        fechaSeleccionada = fecha
                        horaSeleccionada = null
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        fechaSeleccionada?.let {
            Text(
                text = Repositorio.fechaLarga(it),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        Text(
            text = "Selecciona un horario",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (fechaSeleccionada == null) {
            EstadoVacio(
                icono = Icons.Filled.DateRange,
                texto = "Selecciona una fecha para ver los horarios"
            )
        } else if (horariosDisponibles.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.DateRange,
                texto = "No hay horarios disponibles para esta fecha"
            )
        } else {
            Repositorio.horarios.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fila.forEach { hora ->
                        HoraCita(
                            hora = hora,
                            visible = formatearHora(hora),
                            ocupada = hora !in horariosDisponibles,
                            seleccionada = hora == horaSeleccionada,
                            onClick = { horaSeleccionada = hora },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        BotonGradiente(
            texto = "Continuar",
            enabled = fechaSeleccionada != null && horaSeleccionada != null,
            onClick = {
                val fecha = fechaSeleccionada
                val hora = horaSeleccionada
                if (fecha != null && hora != null) onContinuar(medicoId, fecha.toString(), hora)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DiaCita(
    dia: String,
    numero: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(52.dp)
            .height(70.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
        border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(dia, style = MaterialTheme.typography.labelSmall)
            Text(numero, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HoraCita(
    hora: String,
    visible: String,
    ocupada: Boolean,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(enabled = !ocupada, onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = when {
            seleccionada -> MaterialTheme.colorScheme.primary
            ocupada -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surface
        },
        contentColor = when {
            seleccionada -> MaterialTheme.colorScheme.onPrimary
            ocupada -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
            else -> MaterialTheme.colorScheme.onBackground
        },
        border = if (!seleccionada) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        } else {
            null
        }
    ) {
        Text(
            text = visible,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp)
        )
    }
}

private fun formatearHora(hora: String): String {
    val partes = hora.split(":")
    val hora24 = partes.firstOrNull()?.toIntOrNull() ?: return hora
    val minutos = partes.getOrNull(1) ?: "00"
    val sufijo = if (hora24 < 12) "AM" else "PM"
    val hora12 = when (val valor = hora24 % 12) {
        0 -> 12
        else -> valor
    }
    return "%02d:%s %s".format(hora12, minutos, sufijo)
}
