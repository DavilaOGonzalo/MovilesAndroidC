package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FotoPersona(
    url: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 52.dp,
    anillo: Boolean = true
) {
    Surface(
        modifier = modifier.size(tamano),
        shape = CircleShape,
        color = Color.White,
        border = if (anillo) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
        } else {
            null
        }
    ) {
        Box(modifier = Modifier.clip(CircleShape)) {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(tamano)
            )
        }
    }
}
