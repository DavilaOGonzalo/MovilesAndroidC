package com.gonzalo.hospitaltecsup_ia.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gonzalo.hospitaltecsup_ia.data.Doctor
import com.gonzalo.hospitaltecsup_ia.data.DoctorCatalog
import com.gonzalo.hospitaltecsup_ia.navigation.Screen
import kotlinx.coroutines.launch

// Pantalla de inicio: lista de especialidades y médicos disponibles.
// Incluye el menú lateral (NavigationDrawer) para acceder a las demás secciones.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val doctors = DoctorCatalog.doctors
    val specialties = remember {
        listOf("Cardiología", "Pediatría")
    }
    var selectedSpecialty by remember { mutableStateOf<String?>(null) }

    val filteredDoctors = remember(doctors, selectedSpecialty) {
        if (selectedSpecialty == null) doctors else doctors.filter { it.specialty == selectedSpecialty }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerState = drawerState,
                currentRoute = currentRoute,
                onNavigateTo = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Clínica Salud+",
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Text(
                                text = "☰",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Encuentra la especialidad y el médico para tu cita.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Especialidades",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(specialties) { specialty ->
                            SpecialtyChip(
                                specialty = specialty,
                                selected = specialty == selectedSpecialty,
                                onClick = {
                                    selectedSpecialty = if (selectedSpecialty == specialty) {
                                        null
                                    } else {
                                        specialty
                                    }
                                },
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Médicos disponibles",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                items(filteredDoctors, key = { it.id }) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                        onClick = {
                            navController.navigate(
                                Screen.DoctorProfile.createRoute(
                                    doctorId = doctor.id,
                                    name = doctor.name,
                                    specialty = doctor.specialty,
                                    rating = doctor.rating.toFloat(),
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecialtyChip(
    specialty: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
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
        shadowElevation = if (selected) 2.dp else 0.dp,
    ) {
        Text(
            text = specialty,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
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
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(colorForDoctor(doctor.id)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initialsOf(doctor.name),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = doctor.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    shadowElevation = 1.dp,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "★",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF996E00),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doctor.rating.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ">",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
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