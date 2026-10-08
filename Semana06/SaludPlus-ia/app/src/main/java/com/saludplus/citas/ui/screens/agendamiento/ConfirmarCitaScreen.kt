package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FotoPersona
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.estiloEspecialidad
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
    val estilo = estiloEspecialidad(medico?.especialidadId ?: 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Confirmar cita", onVolver = onVolver)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Revisa los detalles antes de confirmar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = estilo.color.copy(alpha = 0.10f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, estilo.color.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FotoPersona(url = medico?.fotoUrl.orEmpty(), tamano = 72.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = medico?.nombre ?: "Médico no encontrado",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = estilo.color.copy(alpha = 0.18f),
                        contentColor = estilo.color
                    ) {
                        Text(
                            text = especialidad?.nombre ?: "Especialidad",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                    if (medico != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "★ ${medico.calificacion} · ${medico.experiencia} años de experiencia",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Detalle de la cita",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetalleFila(
                    icono = Icons.Filled.Favorite,
                    titulo = "Especialidad",
                    valor = especialidad?.nombre ?: "No encontrada"
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetalleFila(
                    icono = Icons.Filled.Person,
                    titulo = "Médico",
                    valor = medico?.nombre ?: "No encontrado"
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetalleFila(
                    icono = Icons.Filled.DateRange,
                    titulo = "Fecha",
                    valor = fechaLarga
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetalleFila(
                    icono = Icons.Filled.Star,
                    titulo = "Hora",
                    valor = hora
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetalleFila(
                    icono = Icons.Filled.Info,
                    titulo = "Clínica",
                    valor = "${Repositorio.clinicaNombre} · ${Repositorio.clinicaDireccion}"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        BotonGradiente(
            texto = "Confirmar cita",
            onClick = {
                val guardada = Repositorio.agendarCita(medicoId, fecha, hora)
                if (guardada) {
                    onConfirmado()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DetalleFila(
    icono: ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.padding(9.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
