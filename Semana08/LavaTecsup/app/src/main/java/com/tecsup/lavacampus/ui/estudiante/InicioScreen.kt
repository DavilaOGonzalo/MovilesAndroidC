package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    usuario: Usuario,
    onNuevoPedido: () -> Unit,
    onPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onDetalle: (Int) -> Unit,
    contentModifier: Modifier = Modifier
) {
    val activo by vm.pedidoActivo.collectAsStateWithLifecycle()
    val recientes by vm.recientes.collectAsStateWithLifecycle()

    Column(
        modifier = contentModifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, ${usuario.nombre.substringBefore(' ')}", style = MaterialTheme.typography.headlineSmall)
                Text("${usuario.codigo} · Tecsup", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = {}) { Icon(Icons.Outlined.NotificationsNone, contentDescription = "Avisos") }
        }
        Text("Tu pedido activo", style = MaterialTheme.typography.titleMedium)
        if (activo == null) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Text("No tienes pedidos activos", modifier = Modifier.padding(20.dp))
            }
        } else {
            val pedido = activo!!
            val estado = EstadoPedido.valueOf(pedido.pedido.estado)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Tu pedido activo", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .82f), style = MaterialTheme.typography.bodySmall)
                        Text("#${pedido.pedido.id.toString().padStart(3, '0')}", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge)
                    }
                    Text(estado.etiqueta, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall)
                    Text("Entrega estimada: ${pedido.pedido.fechaEntrega}", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .86f), style = MaterialTheme.typography.bodySmall)
                    if (estado == EstadoPedido.LISTO) {
                        Text("Recógelo en el casillero ${pedido.pedido.casillero ?: "asignado"}", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodySmall)
                    }
                    StepperEstado(estado, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
        LavaButton(text = "Nuevo pedido", onClick = onNuevoPedido)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickAccessCard(Icons.AutoMirrored.Outlined.ListAlt, "Mis pedidos", "Ver estado", onPedidos, Modifier.weight(1f))
            QuickAccessCard(Icons.Outlined.LocalOffer, "Tarifas", "Precios por kg", onTarifas, Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Últimos pedidos", style = MaterialTheme.typography.titleMedium)
            Text("Ver todos", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
        }
        recientes.forEach { pedido ->
            PedidoCard(pedido = pedido, onClick = { onDetalle(pedido.pedido.id) })
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun QuickAccessCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.padding(start = 7.dp)) {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
