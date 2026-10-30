package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.DatoGrafico

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(Modifier.fillMaxWidth().height(150.dp)) {
            val diameter = minOf(size.width, size.height) * .72f
            val topLeft = androidx.compose.ui.geometry.Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )
            var start = -90f
            datos.filter { it.valor > 0 }.forEach { dato ->
                val sweep = if (total == 0f) 0f else dato.valor / total * 360f
                drawArc(
                    color = Color(dato.colorHex),
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = androidx.compose.ui.geometry.Size(diameter, diameter),
                    style = Stroke(width = 30f)
                )
                start += sweep
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            datos.forEach { dato ->
                Text("${dato.etiqueta}: ${dato.valor.toInt()}", style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
            }
        }
    }
}
