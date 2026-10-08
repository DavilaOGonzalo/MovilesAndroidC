package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicos = if (texto.isBlank()) {
        Repositorio.medicosPorEspecialidad(especialidadId)
    } else {
        Repositorio.buscarMedicos(texto).filter { it.especialidadId == especialidadId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Médicos", onVolver = onVolver)
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Text(
                text = especialidad?.nombre ?: "Especialidad no encontrada",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CampoTexto(
            valor = texto,
            onValorChange = { texto = it },
            etiqueta = "Buscar médico",
            icono = Icons.Filled.Search
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (medicos.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.Search,
                texto = "No se encontraron médicos"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(medicos) { medico ->
                    TarjetaMedico(
                        medico = medico,
                        onClick = { onSeleccionarMedico(medico.id) }
                    )
                }
            }
        }
    }
}
