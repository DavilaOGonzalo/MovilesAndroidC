package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun IconoServicio(icono: String, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    Box(modifier = modifier.size(52.dp)) {
        Canvas(Modifier.size(52.dp)) {
            val stroke = Stroke(width = 3f)
            when (icono) {
                "wash" -> {
                    drawRoundRect(
                        color = primary,
                        topLeft = Offset(size.width * .2f, size.height * .12f),
                        size = Size(size.width * .6f, size.height * .76f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f),
                        style = stroke
                    )
                    drawCircle(primary, radius = size.minDimension * .2f, center = center, style = stroke)
                    drawCircle(primary, radius = 3f, center = Offset(size.width * .35f, size.height * .23f))
                }
                "iron" -> {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * .2f, size.height * .68f)
                        lineTo(size.width * .78f, size.height * .68f)
                        lineTo(size.width * .58f, size.height * .4f)
                        lineTo(size.width * .42f, size.height * .4f)
                        close()
                    }
                    drawPath(path, primary, style = stroke)
                    drawLine(primary, Offset(size.width * .35f, size.height * .28f), Offset(size.width * .58f, size.height * .28f), strokeWidth = 3f, cap = StrokeCap.Round)
                }
                else -> {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * .3f, size.height * .2f)
                        lineTo(size.width * .48f, size.height * .32f)
                        lineTo(size.width * .7f, size.height * .2f)
                        lineTo(size.width * .86f, size.height * .38f)
                        lineTo(size.width * .7f, size.height * .52f)
                        lineTo(size.width * .7f, size.height * .82f)
                        lineTo(size.width * .3f, size.height * .82f)
                        lineTo(size.width * .3f, size.height * .52f)
                        lineTo(size.width * .14f, size.height * .38f)
                        close()
                    }
                    drawPath(path, primary, style = stroke)
                }
            }
        }
    }
}
