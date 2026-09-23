package com.gonzalo.hospitaltecsup_ia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gonzalo.hospitaltecsup_ia.data.Cita
import com.gonzalo.hospitaltecsup_ia.data.CitaCatalog
import com.gonzalo.hospitaltecsup_ia.data.EstadoCita

// Mis citas: lista de citas agendadas accesible desde el menú lateral.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(navController: NavController) {
    val citas = CitaCatalog.citas

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis citas",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = "Tienes ${citas.count { it.estado == EstadoCita.CONFIRMADA }} cita(s) confirmada(s).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(citas, key = { it.id }) { cita ->
                CitaCard(cita = cita)
            }
        }
    }
}

@Composable
private fun CitaCard(cita: Cita) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colorForDoctor(cita.doctorId)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initialsOf(cita.doctorName),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cita.doctorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = cita.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InfoPill(text = cita.date)
                    Spacer(modifier = Modifier.width(8.dp))
                    InfoPill(text = cita.time)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            EstadoBadge(estado = cita.estado)
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun EstadoBadge(estado: EstadoCita) {
    val (label, bgColor, fgColor) = when (estado) {
        EstadoCita.CONFIRMADA -> Triple(
            "Confirmada",
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
        )

        EstadoCita.COMPLETADA -> Triple(
            "Completada",
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor,
        contentColor = fgColor,
        modifier = Modifier.border(
            width = if (estado == EstadoCita.COMPLETADA) 1.dp else 0.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            shape = RoundedCornerShape(50),
        ),
    ) {
        Text(
            text = "✓ $label",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

private fun initialsOf(name: String): String =
    name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

private fun colorForDoctor(id: Int): Color = listOf(
    Color(0xFF7C5CBF),
    Color(0xFF5E6BC0),
    Color(0xFF8A4F9E),
    Color(0xFFB05A8C),
)[(id - 1) % 4]