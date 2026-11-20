package com.davila.campusgps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionadoId: Int?,
    radioMetros: Int,
    onSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = lugares, key = { it.lugar.id }) { item ->
            val seleccionado = item.lugar.id == seleccionadoId
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeleccionar(item.lugar.id) },
                colors = CardDefaults.cardColors(
                    containerColor = if (seleccionado) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(text = item.lugar.nombre, style = MaterialTheme.typography.titleSmall)
                        Text(text = item.lugar.categoria, style = MaterialTheme.typography.bodySmall)
                        if (item.dentroDelRadio == true) {
                            Text("Dentro de $radioMetros m", style = MaterialTheme.typography.labelSmall)
                        }
                        if (item.lugar.posicion == null) {
                            Text(
                                text = item.lugar.referenciaPendiente?.let {
                                    "Código plus pendiente: $it"
                                } ?: "Ubicación interna por confirmar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                    Text(
                        text = if (item.lugar.posicion == null) {
                            "Pendiente"
                        } else {
                            formatearDistancia(item.metros)
                        },
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
