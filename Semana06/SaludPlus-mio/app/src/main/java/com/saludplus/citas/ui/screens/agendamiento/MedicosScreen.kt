package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import com.saludplus.citas.ui.components.TarjetaMedico

@Composable
fun MedicosScreen(especialidadId: Int) {
    var texto by remember { mutableStateOf("") }
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicos = if (texto.isBlank()) {
        Repositorio.medicosPorEspecialidad(especialidadId)
    } else {
        Repositorio.buscarMedicos(texto).filter { it.especialidadId == especialidadId }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Médicos",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = especialidad?.nombre ?: "Especialidad no encontrada",
            style = MaterialTheme.typography.bodyLarge
        )
        CampoTexto(
            valor = texto,
            onValorChange = { texto = it },
            etiqueta = "Buscar médico"
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(medicos) { medico ->
                TarjetaMedico(medico = medico)
            }
        }
    }
}
