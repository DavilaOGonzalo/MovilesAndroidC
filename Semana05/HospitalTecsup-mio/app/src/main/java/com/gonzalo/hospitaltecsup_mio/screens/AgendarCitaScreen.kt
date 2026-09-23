package com.gonzalo.hospitaltecsup_mio.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gonzalo.hospitaltecsup_mio.data.DoctorCatalog
import com.gonzalo.hospitaltecsup_mio.navigation.Screen

// Fechas y horas disponibles (datos estáticos).
private val availableDates = listOf("Lun 12", "Mar 13", "Mié 14")
private val availableTimes = listOf("9:00", "10:30", "12:00", "15:30")

// Pantalla para agendar la cita: selección de fecha y hora.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(navController: NavController, doctorId: Int) {
    val doctor = remember(doctorId) { DoctorCatalog.findById(doctorId) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agendar cita",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            doctor?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(colorForDoctor(doctorId)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = initialsOf(it.name),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = it.specialty,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Selecciona la fecha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(availableDates) { date ->
                    SelectionChip(
                        label = date,
                        selected = date == selectedDate,
                        onClick = {
                            selectedDate = if (selectedDate == date) null else date
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Selecciona la hora",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(availableTimes) { time ->
                    SelectionChip(
                        label = time,
                        selected = time == selectedTime,
                        onClick = {
                            selectedTime = if (selectedTime == time) null else time
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    navController.navigate(
                        Screen.Confirmation.createRoute(
                            doctorId = doctorId,
                            date = selectedDate.orEmpty(),
                            time = selectedTime.orEmpty(),
                        ),
                    )
                },
                enabled = selectedDate != null && selectedTime != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text(
                    text = "Confirmar cita",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun SelectionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
    ) {
        Text(
            text = if (selected) "✓ $label" else label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
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