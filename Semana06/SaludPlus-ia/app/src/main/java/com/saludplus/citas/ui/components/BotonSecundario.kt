package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorError: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (colorError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (colorError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = texto, style = MaterialTheme.typography.labelLarge)
    }
}
