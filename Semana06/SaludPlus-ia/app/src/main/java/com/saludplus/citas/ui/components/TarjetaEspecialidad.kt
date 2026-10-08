package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Especialidad

@Composable
fun TarjetaEspecialidad(
    especialidad: Especialidad,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.width(180.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = especialidad.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = especialidad.descripcion,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
