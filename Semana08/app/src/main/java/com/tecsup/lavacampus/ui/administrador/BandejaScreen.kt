package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    onBack: () -> Unit,
    onGestionar: (Int) -> Unit
) {
    val pedidos by vm.pedidos.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val atrasados by vm.idsAtrasados.collectAsStateWithLifecycle()
    val filtros = listOf("Todos", "Recibido", "En proceso", "Listo")

    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Pedidos", onBack = onBack)
        LavaTextField(
            value = busqueda,
            onValueChange = vm::cambiarBusqueda,
            label = "Buscar por estudiante o número",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtros.size) { index ->
                FilterChip(selected = filtro == filtros[index], onClick = { vm.cambiarFiltro(filtros[index]) }, label = { Text(filtros[index]) })
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(pedidos, key = { it.pedido.id }) { pedido ->
                val estado = EstadoPedido.valueOf(pedido.pedido.estado)
                Column(Modifier.fillMaxWidth()) {
                    Text(pedido.usuario.nombre, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(start = 8.dp))
                    PedidoCard(
                        pedido = pedido,
                        onClick = { onGestionar(pedido.pedido.id) },
                        marcadoAtrasado = pedido.pedido.id in atrasados
                    )
                    if (estado in EstadoPedido.activas) {
                        Button(onClick = { onGestionar(pedido.pedido.id) }, modifier = Modifier.align(Alignment.End)) {
                            Text(
                                when (estado) {
                                    EstadoPedido.RECIBIDO -> "Iniciar"
                                    EstadoPedido.EN_PROCESO -> "Marcar listo"
                                    else -> "Entregar"
                                }
                            )
                        }
                    }
                }
            }
            if (pedidos.isEmpty()) item { Text("No hay pedidos para este filtro", modifier = Modifier.padding(24.dp)) }
        }
    }
}
