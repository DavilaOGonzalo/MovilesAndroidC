package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.components.SelectorCantidad
import com.tecsup.lavacampus.ui.components.formatQuantity
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import java.util.Locale

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    recibirPedidos: Boolean,
    onBack: () -> Unit,
    onConfirmed: () -> Unit
) {
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    val cantidades by vm.cantidades.collectAsStateWithLifecycle()
    val nota by vm.nota.collectAsStateWithLifecycle()
    val total by vm.total.collectAsStateWithLifecycle()
    val subtotal by vm.subtotal.collectAsStateWithLifecycle()
    val descuento by vm.descuento.collectAsStateWithLifecycle()
    val fecha by vm.fechaEntrega.collectAsStateWithLifecycle()
    val puedeConfirmar by vm.puedeConfirmar.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(title = if (vm.esRepetido) "Pedir de nuevo" else "Nuevo pedido", onBack = onBack)
        if (vm.esRepetido) {
            Text("Revisa tus servicios; se aplicarán las tarifas vigentes.", color = MaterialTheme.colorScheme.primary)
        }
        Text("¿Qué necesitas?", style = MaterialTheme.typography.titleMedium)
        servicios.forEach { servicio ->
            ServicioCantidadItem(
                servicio = servicio,
                cantidad = cantidades[servicio.id] ?: 0.0,
                onChange = { delta -> vm.cambiarCantidad(servicio.id, delta) }
            )
        }
        LavaTextField(
            value = nota,
            onValueChange = vm::cambiarNota,
            label = "Nota (opcional)",
            singleLine = false
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Resumen", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)
                servicios.filter { (cantidades[it.id] ?: 0.0) > 0 }.forEach { servicio ->
                    val cantidad = cantidades[servicio.id] ?: 0.0
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${servicio.nombre} · ${formatQuantity(cantidad)} ${servicio.unidad.lowercase()}")
                        Text(String.format(Locale.US, "S/ %.2f", cantidad * servicio.precio))
                    }
                }
                if (descuento > 0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal")
                        Text(String.format(Locale.US, "S/ %.2f", subtotal))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Descuento por volumen (10%)", color = MaterialTheme.colorScheme.primary)
                        Text(String.format(Locale.US, "- S/ %.2f", descuento), color = MaterialTheme.colorScheme.primary)
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text(String.format(Locale.US, "S/ %.2f", total), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
                Text("Entrega estimada: $fecha", style = MaterialTheme.typography.bodySmall)
            }
        }
        if (!recibirPedidos) {
            Text("La lavandería no está recibiendo pedidos", color = MaterialTheme.colorScheme.error)
        }
        if (error != null) {
            Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
        }
        LavaButton(
            text = "Confirmar pedido",
            onClick = { vm.confirmar(onConfirmed) },
            enabled = puedeConfirmar && recibirPedidos
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ServicioCantidadItem(
    servicio: Servicio,
    cantidad: Double,
    onChange: (Double) -> Unit
) {
    val seleccionado = cantidad > 0
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = if (seleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoServicio(servicio.icono)
            Column(Modifier.weight(1f).padding(start = 10.dp)) {
                Text(servicio.nombre, style = MaterialTheme.typography.titleSmall)
                Text(
                    String.format(Locale.US, "S/ %.2f ${servicio.textoPrecio()} · ${servicio.horasEntrega} h", servicio.precio),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SelectorCantidad(
                valor = cantidad,
                paso = if (servicio.unidad == "KG") .5 else 1.0,
                minimo = if (servicio.unidad == "KG") 1.0 else 1.0,
                maximo = if (servicio.unidad == "KG") 10.0 else 30.0,
                onChange = onChange
            )
        }
    }
}
