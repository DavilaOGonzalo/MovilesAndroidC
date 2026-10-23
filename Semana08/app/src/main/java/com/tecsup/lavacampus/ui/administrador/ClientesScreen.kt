package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.ClientesViewModel
import java.util.Locale

@Composable
fun ClientesScreen(
    vm: ClientesViewModel,
    onBack: () -> Unit
) {
    val clientes by vm.clientes.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Clientes", onBack = onBack)
        LavaTextField(busqueda, vm::cambiarBusqueda, "Buscar cliente", Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(clientes, key = { it.id }) { cliente ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                            Text(
                                cliente.nombre.split(" ").take(2).joinToString("") { it.first().uppercase() },
                                modifier = Modifier.padding(13.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(cliente.codigo, style = MaterialTheme.typography.bodySmall)
                            Text(cliente.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${cliente.cantidadPedidos} pedidos", style = MaterialTheme.typography.labelSmall)
                            Text(String.format(Locale.US, "S/ %.2f", cliente.totalGastado), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
