package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.formatQuantity
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import java.util.Locale

@Composable
fun DetallePedidoScreen(
    vm: DetallePedidoViewModel,
    onBack: () -> Unit
) {
    val pedido by vm.pedido.collectAsStateWithLifecycle()
    val puedeCancelar by vm.puedeCancelar.collectAsStateWithLifecycle()
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    if (pedido == null) {
        Text("Cargando pedido…", modifier = Modifier.padding(24.dp))
        return
    }
    val actual = pedido!!
    val estado = EstadoPedido.valueOf(actual.pedido.estado)
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(title = "Pedido #${actual.pedido.id.toString().padStart(3, '0')}", onBack = onBack)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Registrado el ${actual.pedido.fechaRegistro}", style = MaterialTheme.typography.titleSmall)
                Text("Entrega estimada: ${actual.pedido.fechaEntrega}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                EstadoPedidoChip(estado)
            }
        }
        Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            StepperEstado(estado, vertical = true, modifier = Modifier.padding(16.dp))
        }
        Text("Detalle", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                actual.detalles.forEach { linea ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${linea.servicio.nombre} · ${formatQuantity(linea.detalle.cantidad)} × S/ ${String.format(Locale.US, "%.2f", linea.detalle.precioUnitario)}")
                        Text(String.format(Locale.US, "S/ %.2f", linea.detalle.subtotal))
                    }
                }
                if (actual.pedido.descuento > 0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Descuento por volumen")
                        Text(String.format(Locale.US, "- S/ %.2f", actual.pedido.descuento), color = MaterialTheme.colorScheme.primary)
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text(String.format(Locale.US, "S/ %.2f", actual.pedido.total), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = if (actual.pedido.pagado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (actual.pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Text(
            if (estado == EstadoPedido.LISTO) "Casillero: ${actual.pedido.casillero ?: "asignado"}" else "Casillero: se asignará al estar listo",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedButton(
            onClick = { mostrarConfirmacion = true },
            enabled = puedeCancelar,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cancelar pedido") }
        if (!puedeCancelar) {
            Text("Solo se cancela antes de empezar el lavado", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(16.dp))
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("¿Cancelar pedido?") },
            text = { Text("El pedido pasará a estado Cancelado y no podrás reactivarlo.") },
            confirmButton = {
                TextButton(onClick = { mostrarConfirmacion = false; vm.cancelar() }) { Text("Cancelar pedido") }
            },
            dismissButton = { TextButton(onClick = { mostrarConfirmacion = false }) { Text("Volver") } }
        )
    }
}
