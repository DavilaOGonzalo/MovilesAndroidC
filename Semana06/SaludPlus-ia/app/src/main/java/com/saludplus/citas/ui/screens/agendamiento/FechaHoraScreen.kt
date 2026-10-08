package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoConVolver
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (Int, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var semanaBase by remember { mutableStateOf(Repositorio.semanaActual()) }
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val dias = Repositorio.diasHabilesDeLaSemana(semanaBase)
    val horariosDisponibles = fechaSeleccionada
        ?.let { Repositorio.horariosDisponibles(medicoId, it.toString()) }
        ?: emptyList()
    val puedeRetroceder = semanaBase.isAfter(Repositorio.semanaActual())
    val hoy = LocalDate.now()

    fun irASemana(nueva: LocalDate) {
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Fecha y hora", onVolver = onVolver)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Elige el día y la hora de tu cita",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    IconButton(
                        onClick = { irASemana(semanaBase.minusWeeks(1)) },
                        enabled = puedeRetroceder
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Semana anterior"
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Repositorio.nombreMesYAnio(semanaBase),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (dias.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Del ${dias.first().dayOfMonth} al ${dias.last().dayOfMonth}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    IconButton(onClick = { irASemana(semanaBase.plusWeeks(1)) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Semana siguiente"
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Selecciona un día",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            dias.forEach { fecha ->
                val partes = Repositorio.formatearFecha(fecha).split(" ")
                TarjetaDia(
                    diaSemana = partes.getOrElse(0) { "" },
                    numeroDia = partes.getOrElse(1) { "" }.substringBefore("/"),
                    esHoy = fecha == hoy,
                    seleccionada = fecha == fechaSeleccionada,
                    onClick = {
                        fechaSeleccionada = fecha
                        horaSeleccionada = null
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (fechaSeleccionada != null) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = fechaSeleccionada?.let { Repositorio.formatearFecha(it) }.orEmpty(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        when {
            fechaSeleccionada == null -> {
                EstadoVacio(
                    icono = Icons.Filled.DateRange,
                    texto = "Selecciona un día para ver los horarios disponibles"
                )
            }

            horariosDisponibles.isEmpty() -> {
                EstadoVacio(
                    icono = Icons.Filled.DateRange,
                    texto = "No hay horarios disponibles para esta fecha"
                )
            }

            else -> {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Repositorio.horarios.forEach { hora ->
                        val ocupada = hora !in horariosDisponibles
                        ChipHora(
                            hora = hora,
                            seleccionada = hora == horaSeleccionada,
                            ocupada = ocupada,
                            onClick = { horaSeleccionada = hora }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (fechaSeleccionada != null && horaSeleccionada != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = "Cita: ${
                        fechaSeleccionada?.let { Repositorio.formatearFecha(it) }.orEmpty()
                    } · $horaSeleccionada",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        BotonPrincipal(
            texto = "Continuar",
            enabled = fechaSeleccionada != null && horaSeleccionada != null,
            onClick = {
                val fecha = fechaSeleccionada
                val hora = horaSeleccionada
                if (fecha != null && hora != null) {
                    onContinuar(medicoId, fecha.toString(), hora)
                }
            }
        )
    }
}

@Composable
private fun TarjetaDia(
    diaSemana: String,
    numeroDia: String,
    esHoy: Boolean,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo by animateColorAsState(
        targetValue = if (seleccionada) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "fondoDia"
    )
    val contenido = if (seleccionada) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier
            .width(64.dp)
            .height(92.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = fondo),
        border = if (seleccionada) {
            null
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = diaSemana.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = contenido.copy(alpha = if (seleccionada) 0.85f else 0.7f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = numeroDia,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = contenido
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (esHoy) {
                Text(
                    text = "Hoy",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = if (seleccionada) {
                        contenido.copy(alpha = 0.85f)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }
}

@Composable
private fun ChipHora(
    hora: String,
    seleccionada: Boolean,
    ocupada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = when {
        seleccionada -> MaterialTheme.colorScheme.primary
        ocupada -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }
    val contenido = when {
        seleccionada -> MaterialTheme.colorScheme.onPrimary
        ocupada -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.primary
    }
    val borde = when {
        seleccionada -> null
        ocupada -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        else -> BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
    }

    Surface(
        modifier = modifier.then(
            if (ocupada) Modifier else Modifier.clickable(onClick = onClick)
        ),
        shape = RoundedCornerShape(14.dp),
        color = fondo,
        contentColor = contenido,
        border = borde
    ) {
        Text(
            text = hora,
            style = MaterialTheme.typography.labelLarge,
            textDecoration = if (ocupada) TextDecoration.LineThrough else null,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 11.dp)
        )
    }
}

@Composable
private fun EstadoVacio(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
