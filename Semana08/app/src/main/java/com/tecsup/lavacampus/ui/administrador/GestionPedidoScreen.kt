package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import java.util.Locale

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun GestionPedidoScreen(
    vm: GestionPedidoViewModel,
    onBack: () -> Unit
) {
    val pedido by vm.pedido.collectAsStateWithLifecycle()
    val ocupados by vm.casillerosOcupados.collectAsStateWithLifecycle()
    val elegido by vm.casilleroElegido.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    if (pedido == null) {
        Text("Cargando pedido…", modifier = Modifier.padding(24.dp))
        return
    }
    val actual = pedido!!
    val estado = EstadoPedido.valueOf(actual.pedido.estado)
    val siguiente = estado.siguiente()
    val pagado = actual.pedido.pagado

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(title = "Gestionar pedido #${actual.pedido.id.toString().padStart(3, '0')}", onBack = onBack)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(actual.usuario.nombre, style = MaterialTheme.typography.titleMedium)
                Text(actual.usuario.codigo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                EstadoPedidoChip(estado)
            }
        }
        Card(Modifier.fillMaxWidth()) { StepperEstado(estado, vertical = true, modifier = Modifier.padding(16.dp)) }
        if (siguiente == EstadoPedido.LISTO || estado == EstadoPedido.EN_PROCESO) {
            Text("Casillero para el recojo", style = MaterialTheme.typography.titleMedium)
            Text("Los casilleros en gris ya están ocupados", style = MaterialTheme.typography.bodySmall)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Casilleros.todos.forEach { casillero ->
                    val ocupado = casillero in ocupados && casillero != actual.pedido.casillero
                    FilterChip(
                        selected = elegido == casillero,
                        onClick = { vm.elegirCasillero(casillero) },
                        enabled = !ocupado,
                        label = { Text(casillero) }
                    )
                }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Total", style = MaterialTheme.typography.labelMedium)
                    Text(String.format(Locale.US, "S/ %.2f", actual.pedido.total), style = MaterialTheme.typography.titleLarge)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cobrado", modifier = Modifier.padding(top = 12.dp))
                    Switch(checked = pagado, onCheckedChange = vm::marcarPagado)
                }
            }
        }
        Text("No se entrega un pedido sin cobrar ni se marca Listo sin casillero.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (mensaje != null) Text(mensaje.orEmpty(), color = MaterialTheme.colorScheme.error)
        if (siguiente != null) {
            LavaButton(text = "Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar, enabled = estado != EstadoPedido.ENTREGADO)
        }
        if (estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO) {
            OutlinedButton(onClick = vm::cancelar, modifier = Modifier.fillMaxWidth()) { Text("Cancelar pedido") }
        }
        Spacer(Modifier.padding(bottom = 12.dp))
    }
}
