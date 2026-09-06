package com.gonzalo.registrodenotasia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gonzalo.registrodenotasia.ui.theme.RegistroDeNotasIATheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroDeNotasIATheme {
                GradeRegistryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeRegistryScreen() {
    var note1 by remember { mutableFloatStateOf(0f) }
    var note2 by remember { mutableFloatStateOf(0f) }
    var note3 by remember { mutableFloatStateOf(0f) }
    var note4 by remember { mutableFloatStateOf(0f) }
    var roundAverage by remember { mutableStateOf(false) }
    var confirmed by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }

    // Colores personalizados para el prototipo morado
    val PurplePrimary = Color(0xFF673AB7)
    val LightLavender = Color(0xFFF3E5F5)

    // Lógica de cálculo
    val weightedAverage = (note1 * 0.20f) + (note2 * 0.25f) + (note3 * 0.30f) + (note4 * 0.25f)
    
    val finalAverageString = if (roundAverage) {
        val rounded = Math.round(weightedAverage)
        "$rounded (redondeado)"
    } else {
        String.format(Locale.US, "%.2f", weightedAverage)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Registro de Notas", 
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurplePrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(LightLavender, Color.White)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Notas del ciclo",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurplePrimary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    text = "Desliza para asignar una nota (0 a 20)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CourseGradeSlider("Fundamentos de Programación (20%)", note1, PurplePrimary) { note1 = it }
                CourseGradeSlider("Programación Orientada a Objetos (25%)", note2, PurplePrimary) { note2 = it }
                CourseGradeSlider("Programación en Móviles (30%)", note3, PurplePrimary) { note3 = it }
                CourseGradeSlider("Base de Datos (25%)", note4, PurplePrimary) { note4 = it }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Redondear promedio final", 
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp
                    )
                    Switch(
                        checked = roundAverage, 
                        onCheckedChange = { roundAverage = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PurplePrimary,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = confirmed, 
                        onCheckedChange = { confirmed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PurplePrimary
                        )
                    )
                    Text(
                        text = "Confirmo que las notas son correctas",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showResults = true },
                    enabled = confirmed,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurplePrimary,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!showResults) {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    ResultsCard(weightedAverage, finalAverageString, PurplePrimary)
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Desarrollado por: Gonzalo Davila Ochochoque",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CourseGradeSlider(courseName: String, value: Float, color: Color, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = courseName, 
                modifier = Modifier.weight(1f), 
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Surface(
                color = color,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = value.toInt().toString(),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..20f,
            steps = 19,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = color.copy(alpha = 0.24f)
            )
        )
    }
}

@Composable
fun ResultsCard(weightedAverage: Float, finalAverage: String, purpleColor: Color) {
    val observation: String
    val observationColor: Color
    val observationBg: Color

    when {
        weightedAverage >= 17 -> {
            observation = "EXCELENTE"
            observationColor = Color(0xFF1B5E20) // Verde oscuro
            observationBg = Color(0xFFC8E6C9)
        }
        weightedAverage >= 13 -> {
            observation = "APROBADO"
            observationColor = Color(0xFF2E7D32) // Verde
            observationBg = Color(0xFFE8F5E9)
        }
        weightedAverage >= 10 -> {
            observation = "EN RECUPERACIÓN"
            observationColor = Color(0xFFE65100) // Ámbar/Naranja
            observationBg = Color(0xFFFFF3E0)
        }
        else -> {
            observation = "DESAPROBADO"
            observationColor = Color(0xFFB71C1C) // Rojo
            observationBg = Color(0xFFFFEBEE)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✓ Promedio calculado correctamente",
                color = Color(0xFF4CAF50),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Promedio ponderado: ${String.format(Locale.US, "%.2f", weightedAverage)}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text("PROMEDIO FINAL", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(
                text = finalAverage, 
                style = MaterialTheme.typography.headlineLarge, 
                color = purpleColor, 
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Surface(
                color = observationBg,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, observationColor)
            ) {
                Text(
                    text = observation,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                    color = observationColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GradeRegistryPreview() {
    RegistroDeNotasIATheme {
        GradeRegistryScreen()
    }
}
