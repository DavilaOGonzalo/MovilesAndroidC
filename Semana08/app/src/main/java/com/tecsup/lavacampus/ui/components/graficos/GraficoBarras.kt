package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.DatoGrafico
import java.util.Locale

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = datos.maxOfOrNull { it.valor }?.coerceAtLeast(1f) ?: 1f
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(150.dp).padding(horizontal = 12.dp)) {
            val ancho = if (datos.isEmpty()) size.width else size.width / datos.size * .54f
            datos.forEachIndexed { index, dato ->
                val x = (index + .5f) * size.width / datos.size - ancho / 2
                val alto = size.height * (dato.valor / maximo)
                drawRoundRect(
                    color = Color(dato.colorHex),
                    topLeft = androidx.compose.ui.geometry.Offset(x, size.height - alto),
                    size = androidx.compose.ui.geometry.Size(ancho, alto),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            datos.forEach { dato ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(dato.etiqueta, style = MaterialTheme.typography.labelSmall)
                    Text(String.format(Locale.US, "S/ %.0f", dato.valor), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
