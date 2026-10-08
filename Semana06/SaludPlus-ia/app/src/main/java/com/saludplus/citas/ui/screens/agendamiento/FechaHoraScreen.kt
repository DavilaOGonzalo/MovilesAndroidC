package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.OpcionSeleccionable
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (Int, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var semanaBase by remember { mutableStateOf(Repositorio.semanaActual()) }
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val dias = Repositorio.diasHabilesDeLaSemana(semanaBase)
    val horarios = fechaSeleccionada
        ?.let { Repositorio.horariosDisponibles(medicoId, it) }
        ?: emptyList()
    val puedeRetroceder = semanaBase.isAfter(Repositorio.semanaActual())

    fun irASemana(nueva: LocalDate) {
        semanaBase = nueva
        val etiquetas = Repositorio.diasHabilesDeLaSemana(nueva)
            .map { Repositorio.formatearFecha(it) }
        if (fechaSeleccionada !in etiquetas) {
            fechaSeleccionada = null
            horaSeleccionada = null
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        EncabezadoConVolver(titulo = "Fecha y hora", onVolver = onVolver)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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
            Text(
                text = Repositorio.nombreMesYAnio(semanaBase),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { irASemana(semanaBase.plusWeeks(1)) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Semana siguiente"
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Fecha", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 160.dp)
        ) {
            items(dias) { fecha ->
                val etiqueta = Repositorio.formatearFecha(fecha)
                OpcionSeleccionable(
                    texto = etiqueta,
                    seleccionada = etiqueta == fechaSeleccionada,
                    onClick = {
                        fechaSeleccionada = etiqueta
                        horaSeleccionada = null
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Horarios disponibles", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (fechaSeleccionada == null) {
            Text(
                text = "Selecciona una fecha para ver los horarios",
                style = MaterialTheme.typography.bodyMedium
            )
        } else if (horarios.isEmpty()) {
            Text(
                text = "No hay horarios disponibles para esta fecha",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items(horarios) { hora ->
                    OpcionSeleccionable(
                        texto = hora,
                        seleccionada = hora == horaSeleccionada,
                        onClick = { horaSeleccionada = hora }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BotonPrincipal(
            texto = "Continuar",
            enabled = fechaSeleccionada != null && horaSeleccionada != null,
            onClick = {
                val fecha = fechaSeleccionada
                val hora = horaSeleccionada
                if (fecha != null && hora != null) {
                    onContinuar(medicoId, fecha, hora)
                }
            }
        )
    }
}
