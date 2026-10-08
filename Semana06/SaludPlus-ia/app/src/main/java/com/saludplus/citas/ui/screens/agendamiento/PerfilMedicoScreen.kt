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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.estiloEspecialidad
import com.saludplus.citas.ui.theme.DisponibleVerde
import com.saludplus.citas.ui.theme.RatingAmber

@Composable
fun PerfilMedicoScreen(
    medicoId: Int,
    onAgendar: () -> Unit,
    onVolver: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        EncabezadoConVolver(titulo = "Detalle del médico", onVolver = onVolver)
        Spacer(modifier = Modifier.height(12.dp))
        if (medico == null) {
            EstadoVacio(
                icono = Icons.Filled.Info,
                texto = "No se encontró la información del médico"
            )
        } else {
            val especialidad = Repositorio.obtenerEspecialidad(medico.especialidadId)
            val estilo = estiloEspecialidad(medico.especialidadId)
            val disponibilidad = Repositorio.proximaDisponibilidad(medico.id)

            AsyncImage(
                model = medico.fotoUrl,
                contentDescription = medico.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = medico.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = especialidad?.nombre ?: "Especialidad",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = RatingAmber,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " ${medico.calificacion} (${medico.resenas} reseñas)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            InfoFila(
                icono = Icons.Filled.DateRange,
                texto = "${medico.experiencia} años de experiencia",
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoFila(
                icono = Icons.Filled.Build,
                texto = "${Repositorio.clinicaNombre} · ${Repositorio.clinicaDireccion}",
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoFila(
                icono = estilo.icono,
                texto = especialidad?.descripcion ?: "Atención médica especializada",
                color = estilo.color.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sobre mí",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Médico ${especialidad?.nombre?.lowercase() ?: "especialista"} con amplia experiencia en prevención, diagnóstico y tratamiento de enfermedades.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = if (disponibilidad.startsWith("Disponible")) {
                    DisponibleVerde.copy(alpha = 0.13f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (disponibilidad.startsWith("Disponible")) {
                    DisponibleVerde
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            ) {
                Text(
                    text = disponibilidad,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            BotonGradiente(texto = "Agendar cita", onClick = onAgendar)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun InfoFila(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    color: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = color,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = texto, style = MaterialTheme.typography.bodySmall)
        }
    }
}
