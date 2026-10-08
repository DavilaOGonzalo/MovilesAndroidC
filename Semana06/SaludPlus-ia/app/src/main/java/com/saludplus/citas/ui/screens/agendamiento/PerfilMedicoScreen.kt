package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.FotoPersona
import com.saludplus.citas.ui.components.estiloEspecialidad
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Perfil médico", onVolver = onVolver)

        if (medico == null) {
            Spacer(modifier = Modifier.height(16.dp))
            EstadoVacio(
                icono = Icons.Filled.Person,
                texto = "No se encontró la información del médico"
            )
            return@Column
        }

        val especialidad = Repositorio.obtenerEspecialidad(medico.especialidadId)
        val estilo = estiloEspecialidad(medico.especialidadId)
        val disponible = Repositorio.proximaDisponibilidad(medico.id)
        val estaDisponible = disponible.startsWith("Disponible")

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FotoPersona(url = medico.fotoUrl, tamano = 132.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = medico.nombre,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = estilo.color.copy(alpha = 0.15f),
                contentColor = estilo.color
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = estilo.icono,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = especialidad?.nombre ?: "Especialidad",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = if (estaDisponible) {
                    Color(0xFF2E9E5B).copy(alpha = 0.14f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (estaDisponible) {
                    Color(0xFF2E9E5B)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = disponible, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaEstadistica(
                modifier = Modifier.weight(1f),
                icono = Icons.Filled.Star,
                colorIcono = RatingAmber,
                valor = medico.calificacion.toString(),
                etiqueta = "Calificación"
            )
            TarjetaEstadistica(
                modifier = Modifier.weight(1f),
                icono = Icons.Filled.Build,
                colorIcono = MaterialTheme.colorScheme.primary,
                valor = "${medico.experiencia} años",
                etiqueta = "Experiencia"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sobre el especialista",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DetalleFila(
                    icono = Icons.Filled.Build,
                    titulo = "Especialidad",
                    valor = especialidad?.nombre ?: "No disponible"
                )
                DetalleFila(
                    icono = Icons.Filled.Info,
                    titulo = "Enfoque",
                    valor = especialidad?.descripcion ?: "Atención médica especializada"
                )
                DetalleFila(
                    icono = Icons.Filled.DateRange,
                    titulo = "Centro de atención",
                    valor = "${Repositorio.clinicaNombre} · ${Repositorio.clinicaDireccion}"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        BotonGradiente(texto = "Agendar cita", onClick = onAgendar)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TarjetaEstadistica(
    icono: ImageVector,
    colorIcono: Color,
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = colorIcono.copy(alpha = 0.15f),
                contentColor = colorIcono,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.padding(9.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
