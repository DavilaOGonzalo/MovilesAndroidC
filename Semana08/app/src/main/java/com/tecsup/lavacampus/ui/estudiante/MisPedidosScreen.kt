package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onBack: () -> Unit,
    onDetalle: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit
) {
    val activos by vm.activos.collectAsStateWithLifecycle()
    val historial by vm.historial.collectAsStateWithLifecycle()
    var tab by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }
    val lista = if (tab == 0) activos else historial

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar(title = "Mis pedidos", onBack = onBack)
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Activos") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Historial") })
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(lista, key = { it.pedido.id }) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        onClick = { onDetalle(pedido.pedido.id) },
                        onCancel = if (tab == 0) ({ porCancelar = pedido }) else null,
                        onRepeat = if (tab == 1) ({ onRepetir(pedido.pedido.id) }) else null
                    )
                }
                if (lista.isEmpty()) {
                    item { Text("No hay pedidos en esta sección", modifier = Modifier.padding(24.dp)) }
                }
            }
        }
        FloatingActionButton(
            onClick = onNuevoPedido,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Nuevo pedido") }
    }

    porCancelar?.let { pedido ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("¿Cancelar pedido #${pedido.pedido.id.toString().padStart(3, '0')}?") },
            text = { Text("Solo puedes cancelar antes de que empiece el lavado.") },
            confirmButton = {
                TextButton(onClick = { vm.cancelar(pedido); porCancelar = null }) { Text("Confirmar") }
            },
            dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("Volver") } }
        )
    }
}
