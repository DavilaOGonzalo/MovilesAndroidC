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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.FotoPersona

@Composable
fun MotivoLugarScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onContinuar: (Int, String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var motivo by remember { mutableStateOf("Control médico") }
    var lugar by remember { mutableStateOf("${Repositorio.clinicaNombre} - Sede Principal") }
    var motivoAbierto by remember { mutableStateOf(false) }
    var lugarAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        EncabezadoConVolver(titulo = "Motivo y lugar", onVolver = onVolver)
        Spacer(modifier = Modifier.height(12.dp))
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
                FotoPersona(url = medico?.fotoUrl.orEmpty(), tamano = 46.dp, anillo = false)
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
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Motivo de la consulta",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        SelectorCita(
            icono = Icons.Filled.Info,
            valor = motivo,
            abierto = motivoAbierto,
            opciones = listOf(
                "Control médico",
                "Dolor en el pecho",
                "Presión alta",
                "Chequeo general",
                "Otro"
            ),
            onAbrir = { motivoAbierto = !motivoAbierto },
            onSeleccionar = {
                motivo = it
                motivoAbierto = false
            }
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Lugar de atención",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        SelectorCita(
            icono = Icons.Filled.Build,
            valor = lugar,
            abierto = lugarAbierto,
            opciones = listOf(
                "${Repositorio.clinicaNombre} - Sede Principal",
                "Consulta virtual"
            ),
            onAbrir = { lugarAbierto = !lugarAbierto },
            onSeleccionar = {
                lugar = it
                lugarAbierto = false
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${Repositorio.fechaLarga(java.time.LocalDate.parse(fecha))} · ${formatearHoraCorta(hora)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        BotonGradiente(
            texto = "Continuar",
            onClick = { onContinuar(medicoId, fecha, hora, motivo, lugar) }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun SelectorCita(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    valor: String,
    abierto: Boolean,
    opciones: List<String>,
    onAbrir: () -> Unit,
    onSeleccionar: (String) -> Unit
) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAbrir),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = valor, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                Text(
                    text = if (abierto) "⌃" else "⌄",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (abierto) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column {
                    opciones.forEach { opcion ->
                        Text(
                            text = opcion,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSeleccionar(opcion) }
                                .padding(horizontal = 14.dp, vertical = 11.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatearHoraCorta(hora: String): String {
    val partes = hora.split(":")
    val hora24 = partes.firstOrNull()?.toIntOrNull() ?: return hora
    val sufijo = if (hora24 < 12) "AM" else "PM"
    val hora12 = if (hora24 % 12 == 0) 12 else hora24 % 12
    return "%02d:%s %s".format(hora12, partes.getOrElse(1) { "00" }, sufijo)
}
