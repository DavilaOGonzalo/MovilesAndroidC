package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import java.util.Locale

@Composable
fun TarifasScreen(
    vm: ServiciosViewModel,
    onBack: () -> Unit
) {
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        AppTopBar(title = "Tarifas y servicios", onBack = onBack)
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios.filter { it.activo }, key = { it.id }) { servicio ->
                Card(Modifier.fillMaxWidth()) {
                    androidx.compose.foundation.layout.Row(Modifier.padding(16.dp)) {
                        IconoServicio(servicio.icono)
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(
                                String.format(Locale.US, "S/ %.2f ${servicio.textoPrecio()}", servicio.precio),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("Entrega en ${servicio.horasEntrega} h", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Horario de atención", style = MaterialTheme.typography.titleSmall)
                        Text("Lunes a sábado · 8:00 a 18:00")
                        Text("Mínimo 1 kg por servicio de lavado. Máximo 3 pedidos activos por estudiante.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
