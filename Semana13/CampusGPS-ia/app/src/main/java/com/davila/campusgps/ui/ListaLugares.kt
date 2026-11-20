package com.davila.campusgps.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.util.formatearDistancia
import com.davila.campusgps.ui.theme.CampusGpsDimensions

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
        verticalArrangement = Arrangement.spacedBy(CampusGpsDimensions.CompactGap),
    ) {
        items(items = lugares, key = { it.lugar.id }) { item ->
            val seleccionado = item.lugar.id == seleccionadoId
            val colorCategoria = colorCategoria(item.lugar)
            val filtroCategoria = CategoriaFiltro.entries.firstOrNull {
                it.categoria == item.lugar.categoria
            } ?: CategoriaFiltro.TODOS
            val cardColor by animateColorAsState(
                targetValue = if (seleccionado) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                animationSpec = tween(durationMillis = 220),
                label = "color-lugar-${item.lugar.id}",
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable { onSeleccionar(item.lugar.id) },
                colors = CardDefaults.elevatedCardColors(containerColor = cardColor),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (seleccionado) 3.dp else 1.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = CircleShape,
                        color = colorCategoria.copy(alpha = 0.13f),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = iconoCategoria(filtroCategoria),
                                contentDescription = null,
                                tint = colorCategoria,
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.lugar.nombre,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (item.dentroDelRadio == true) {
                            Text(
                                text = "Dentro de $radioMetros m",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                        if (item.lugar.posicion == null) {
                            Text(
                                text = item.lugar.referenciaPendiente?.let { "Código plus pendiente: $it" }
                                    ?: "Ubicación interna por confirmar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (item.lugar.posicion == null) "Pendiente" else formatearDistancia(item.metros),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "Lugar seleccionado",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
