package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoSaludPlus(
    modifier: Modifier = Modifier,
    tamano: Dp = 44.dp,
    mostrarNombre: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(tamano), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "SaludPlus",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "+",
                color = Color.White,
                fontSize = (tamano.value * 0.52f).sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (mostrarNombre) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SaludPlus",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
