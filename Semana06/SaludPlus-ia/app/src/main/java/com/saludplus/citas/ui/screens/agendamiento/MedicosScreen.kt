package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.TarjetaMedico

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onSeleccionarMedico: (Int) -> Unit,
    onVolver: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf("Todos") }
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicosFiltrados = Repositorio.medicosPorEspecialidad(especialidadId)
        .filter { medico ->
            texto.isBlank() || medico.nombre.contains(texto, ignoreCase = true)
        }
    val medicos = when (filtro) {
        "Disponibles" -> medicosFiltrados.filter {
            Repositorio.proximaDisponibilidad(it.id).startsWith("Disponible")
        }
        "Mejor valorados" -> medicosFiltrados.sortedByDescending { it.calificacion }
        else -> medicosFiltrados
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        EncabezadoConVolver(
            titulo = especialidad?.nombre ?: "Médicos",
            onVolver = onVolver
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${medicosFiltrados.size} médicos disponibles",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        CampoTexto(
            valor = texto,
            onValorChange = { texto = it },
            etiqueta = "Buscar médico...",
            icono = Icons.Filled.Search
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Todos", "Disponibles", "Mejor valorados").forEach { opcion ->
                FilterChip(
                    selected = filtro == opcion,
                    onClick = { filtro = opcion },
                    label = { Text(opcion) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (medicos.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.Search,
                texto = "No se encontraron médicos"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(9.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(medicos) { medico: Medico ->
                    TarjetaMedico(
                        medico = medico,
                        onClick = { onSeleccionarMedico(medico.id) }
                    )
                }
            }
        }
    }
}
