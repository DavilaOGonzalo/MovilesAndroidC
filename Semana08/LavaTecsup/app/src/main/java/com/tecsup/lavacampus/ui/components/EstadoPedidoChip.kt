package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.theme.colorEnTema

@Composable
fun EstadoPedidoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    val oscuro = MaterialTheme.colorScheme.background.red < .5f
    val color = estado.colorEnTema(oscuro)
    Box(
        modifier = modifier
            .background(color.copy(alpha = .14f), RoundedCornerShape(50))
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) { Text(estado.etiqueta, color = color, style = MaterialTheme.typography.labelSmall) }
}
