package com.davila.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davila.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Historial", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
            if (avisos.isEmpty()) Text("Sin avisos todavía")
            avisos.take(20).forEach { aviso ->
                HorizontalDivider()
                Text(aviso.origen.texto, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                Text(aviso.titulo, style = MaterialTheme.typography.titleSmall)
                Text(aviso.texto)
                Text(aviso.hora, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
