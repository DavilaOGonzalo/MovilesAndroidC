package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import java.util.Locale

@Composable
fun PedidoCard(
    pedido: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null,
    onRepeat: (() -> Unit)? = null,
    marcadoAtrasado: Boolean = false
) {
    val estado = EstadoPedido.valueOf(pedido.pedido.estado)
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (marcadoAtrasado) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoServicio(
                pedido.detalles.firstOrNull()?.servicio?.icono ?: "wash",
                modifier = Modifier.size(48.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text("Pedido #${pedido.pedido.id.toString().padStart(3, '0')}", style = MaterialTheme.typography.titleSmall)
                Text(
                    pedido.detalles.joinToString(" + ") { "${formatQuantity(it.detalle.cantidad)} ${it.servicio.unidad.lowercase()}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    EstadoPedidoChip(estado)
                    if (marcadoAtrasado) {
                        Text("Atrasado", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    String.format(Locale.US, "S/ %.2f", pedido.pedido.total),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
                if (estado == EstadoPedido.LISTO && pedido.pedido.casillero != null) {
                    Text("Casillero ${pedido.pedido.casillero}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        if (onCancel != null && estado == EstadoPedido.RECIBIDO) {
            TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.End)) { Text("Cancelar") }
        }
        if (onRepeat != null && estado == EstadoPedido.ENTREGADO) {
            TextButton(onClick = onRepeat, modifier = Modifier.align(Alignment.End)) { Text("Pedir de nuevo") }
        }
        Spacer(Modifier.size(2.dp))
    }
}

fun formatQuantity(cantidad: Double): String =
    if (cantidad % 1.0 == 0.0) cantidad.toInt().toString()
    else String.format(Locale.US, "%.1f", cantidad)
