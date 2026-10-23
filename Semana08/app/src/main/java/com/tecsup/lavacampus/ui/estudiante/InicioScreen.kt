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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
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
        modifier = contentModifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(title = "Hola, ${usuario.nombre.substringBefore(' ')}", showNotifications = true)
        Text(usuario.codigo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pedido #${pedido.pedido.id.toString().padStart(3, '0')}", color = Color.White)
                        EstadoPedidoChip(estado)
                    }
                    Text(estado.etiqueta, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Text("Entrega estimada: ${pedido.pedido.fechaEntrega}", color = Color.White.copy(alpha = .9f))
                    if (estado == EstadoPedido.LISTO) {
                        Text("Recógelo en el casillero ${pedido.pedido.casillero ?: "asignado"}", color = Color.White)
                    }
                    StepperEstado(estado, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        LavaButton(text = "Nuevo pedido", onClick = onNuevoPedido)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onPedidos, modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.ListAlt, contentDescription = null)
                Text("  Mis pedidos")
            }
            OutlinedButton(onClick = onTarifas, modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.LocalOffer, contentDescription = null)
                Text("  Tarifas")
            }
        }
        Text("Últimos pedidos", style = MaterialTheme.typography.titleMedium)
        recientes.forEach { pedido ->
            PedidoCard(pedido = pedido, onClick = { onDetalle(pedido.pedido.id) })
        }
        Spacer(Modifier.height(12.dp))
    }
}
