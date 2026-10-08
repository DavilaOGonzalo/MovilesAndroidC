package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.OpcionSeleccionable

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (Int, String, String) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val horarios = fechaSeleccionada
        ?.let { Repositorio.horariosDisponibles(medicoId, it) }
        ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Selecciona fecha y hora",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Fecha", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 160.dp)
        ) {
            items(Repositorio.fechasDisponibles) { fecha ->
                OpcionSeleccionable(
                    texto = fecha,
                    seleccionada = fecha == fechaSeleccionada,
                    onClick = {
                        fechaSeleccionada = fecha
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
