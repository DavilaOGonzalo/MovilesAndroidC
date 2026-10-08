package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.TarjetaSeccion
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Confirmar cita",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        TarjetaSeccion(
            titulo = "Especialidad",
            descripcion = especialidad?.nombre ?: "No encontrada"
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaSeccion(
            titulo = "Médico",
            descripcion = medico?.nombre ?: "No encontrado"
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaSeccion(titulo = "Fecha", descripcion = fechaLarga)
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaSeccion(titulo = "Hora", descripcion = hora)
        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Confirmar cita",
            onClick = {
                val guardada = Repositorio.agendarCita(medicoId, fecha, hora)
                if (guardada) {
                    onConfirmado()
                }
            }
        )
        TextButton(onClick = onVolver) {
            Text("Volver")
        }
    }
}
