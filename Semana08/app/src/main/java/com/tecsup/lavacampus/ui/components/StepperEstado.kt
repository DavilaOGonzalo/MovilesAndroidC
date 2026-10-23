package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido

private val etapas = listOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.EN_PROCESO,
    EstadoPedido.LISTO,
    EstadoPedido.ENTREGADO
)

@Composable
fun StepperEstado(
    estado: EstadoPedido,
    vertical: Boolean = false,
    modifier: Modifier = Modifier
) {
    val estadoActual = estadoIndex(estado)
    val estadoColor = Color(estado.colorHex)
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val surface = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline
    if (vertical) {
        Column(modifier = modifier) {
            etapas.forEachIndexed { index, etapa ->
                val completado = index <= estadoActual
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(Modifier.width(34.dp).height(58.dp)) {
                        val x = size.width / 2f
                        if (index < etapas.lastIndex) {
                            drawLine(
                                color = if (index < estadoActual) estadoColor else outlineVariant,
                                start = androidx.compose.ui.geometry.Offset(x, size.height * .5f),
                                end = androidx.compose.ui.geometry.Offset(x, size.height),
                                strokeWidth = 5f
                            )
                        }
                        drawCircle(
                            color = if (completado) estadoColor else surface,
                            radius = 9f,
                            center = androidx.compose.ui.geometry.Offset(x, size.height * .22f)
                        )
                        drawCircle(
                            color = if (completado) estadoColor else outline,
                            radius = 9f,
                            center = androidx.compose.ui.geometry.Offset(x, size.height * .22f),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                        )
                    }
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            etapa.etiqueta,
                            style = if (index == estadoActual) MaterialTheme.typography.titleSmall
                            else MaterialTheme.typography.bodyMedium,
                            color = if (completado) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            if (completado) "Completado" else "Pendiente",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            Canvas(Modifier.fillMaxWidth().height(34.dp)) {
                val spacing = size.width / (etapas.size - 1)
                for (index in 0 until etapas.lastIndex) {
                    drawLine(
                        color = if (index < estadoActual) estadoColor else outlineVariant,
                        start = androidx.compose.ui.geometry.Offset(spacing * index, size.height / 2),
                        end = androidx.compose.ui.geometry.Offset(spacing * (index + 1), size.height / 2),
                        strokeWidth = 5f
                    )
                }
                etapas.forEachIndexed { index, _ ->
                    val done = index <= estadoActual
                    drawCircle(
                        color = if (done) estadoColor else surface,
                        radius = 10f,
                        center = androidx.compose.ui.geometry.Offset(spacing * index, size.height / 2)
                    )
                    drawCircle(
                        color = if (done) estadoColor else outline,
                        radius = 10f,
                        center = androidx.compose.ui.geometry.Offset(spacing * index, size.height / 2),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                etapas.forEach { etapa ->
                    Text(etapa.etiqueta, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

private fun estadoIndex(estado: EstadoPedido): Int = when (estado) {
    EstadoPedido.CANCELADO -> -1
    else -> etapas.indexOf(estado)
}
