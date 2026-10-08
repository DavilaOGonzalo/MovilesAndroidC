package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FotoPersona
import com.saludplus.citas.ui.components.LogoSaludPlus
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.components.TarjetaMedicoDestacado
import com.saludplus.citas.ui.theme.MockupLavender
import com.saludplus.citas.ui.theme.MockupPeach
import com.saludplus.citas.ui.theme.MockupPink
import java.time.LocalDate

@Composable
fun HomeScreen(
    onIrEspecialidades: () -> Unit,
    onIrFichaMedico: (Int) -> Unit,
    onIrCitas: () -> Unit = {},
    onIrResultados: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val especialidades = Repositorio.especialidades.take(6)
    val medicosDestacados = Repositorio.medicos.sortedByDescending { it.calificacion }.take(4)
    val proximaCita = Repositorio.citasDelUsuario()
        .sortedBy { it.fecha }
        .firstOrNull { cita ->
            runCatching { !LocalDate.parse(cita.fecha).isBefore(LocalDate.now()) }
                .getOrDefault(true)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, contentDescription = "Menú")
            }
            LogoSaludPlus(
                modifier = Modifier.weight(1f),
                tamano = 34.dp
            )
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "¡Hola, ${usuario?.nombre ?: "Gonzalo"}!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "¿Qué deseas hacer hoy?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onIrEspecialidades),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Buscar médico, especialidad...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Agendar cita",
                    icono = Icons.Filled.DateRange,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = onIrEspecialidades
                )
                ActionCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Mis citas",
                    icono = Icons.AutoMirrored.Filled.List,
                    color = Color(0xFFE2F9EF),
                    tint = Color(0xFF159B67),
                    onClick = onIrCitas
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Mis doctores",
                    icono = Icons.Filled.Person,
                    color = MockupLavender,
                    tint = Color(0xFF7655D8),
                    onClick = {
                        Repositorio.medicos.firstOrNull()?.let { onIrFichaMedico(it.id) }
                    }
                )
                ActionCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Reportes",
                    icono = Icons.Filled.Info,
                    color = MockupPeach,
                    tint = Color(0xFFF08A24),
                    onClick = onIrResultados
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onIrEspecialidades)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                especialidades.take(3).forEach { especialidad ->
                    TarjetaEspecialidad(
                        especialidad = especialidad,
                        compacta = true,
                        modifier = Modifier.weight(1f),
                        onClick = onIrEspecialidades
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                especialidades.drop(3).take(3).forEach { especialidad ->
                    TarjetaEspecialidad(
                        especialidad = especialidad,
                        compacta = true,
                        modifier = Modifier.weight(1f),
                        onClick = onIrEspecialidades
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Próximas citas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (proximaCita != null) {
                val medico = Repositorio.obtenerMedico(proximaCita.medicoId)
                val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
                val fecha = runCatching { LocalDate.parse(proximaCita.fecha) }
                    .getOrNull()
                    ?.let { Repositorio.fechaLarga(it) }
                    ?: proximaCita.fecha
                CitaResumen(
                    nombre = medico?.nombre ?: "Médico",
                    especialidad = especialidad?.nombre ?: "Cita médica",
                    fotoUrl = medico?.fotoUrl.orEmpty(),
                    detalle = "${fecha} · ${proximaCita.hora}",
                    onClick = onIrCitas
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onIrEspecialidades),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Aún no tienes citas", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Agenda una cita con un especialista",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionCard(
    titulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(84.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CitaResumen(
    nombre: String,
    especialidad: String,
    fotoUrl: String,
    detalle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoPersona(url = fotoUrl, tamano = 48.dp, anillo = false)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(nombre, style = MaterialTheme.typography.titleSmall)
                Text(especialidad, style = MaterialTheme.typography.bodySmall)
                Text(
                    detalle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
