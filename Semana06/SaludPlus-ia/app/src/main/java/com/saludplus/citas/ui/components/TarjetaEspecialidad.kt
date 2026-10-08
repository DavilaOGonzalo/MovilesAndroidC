package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Especialidad

@Composable
fun TarjetaEspecialidad(
    especialidad: Especialidad,
    modifier: Modifier = Modifier,
    compacta: Boolean = false,
    onClick: () -> Unit = {}
) {
    val estilo = estiloEspecialidad(especialidad.id)

    Card(
        modifier = modifier
            .then(
                if (compacta) {
                    Modifier.size(width = 155.dp, height = 100.dp)
                } else {
                    Modifier.size(width = 220.dp, height = 120.dp)
                }
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = estilo.color.copy(alpha = 0.10f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, estilo.color.copy(alpha = 0.25f))
    ) {
        if (compacta) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = estilo.color.copy(alpha = 0.18f),
                    contentColor = estilo.color,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = estilo.icono,
                        contentDescription = null,
                        modifier = Modifier.padding(9.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = especialidad.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        } else {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = estilo.color.copy(alpha = 0.18f),
                    contentColor = estilo.color,
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = estilo.icono,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = especialidad.nombre,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = especialidad.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                }
            }
        }
    }
}
