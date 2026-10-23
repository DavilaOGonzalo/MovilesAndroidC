package com.tecsup.lavacampus.ui.administrador

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.graficos.GraficoBarras
import com.tecsup.lavacampus.ui.components.graficos.GraficoDona
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import java.util.Locale

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPedidos: () -> Unit,
    contentModifier: Modifier = Modifier
) {
    val hoy by vm.pedidosHoy.collectAsStateWithLifecycle()
    val proceso by vm.enProceso.collectAsStateWithLifecycle()
    val recoger by vm.porRecoger.collectAsStateWithLifecycle()
    val atrasados by vm.atrasados.collectAsStateWithLifecycle()
    val ingresos by vm.ingresosSemana.collectAsStateWithLifecycle()
    val porEstado by vm.porEstado.collectAsStateWithLifecycle()
    val porServicio by vm.porServicio.collectAsStateWithLifecycle()

    Column(
        modifier = contentModifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(title = "Panel de la lavandería", showNotifications = true)
        Text("Hoy · ${java.time.LocalDate.now()}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ContadorCard(hoy.toString(), "Pedidos hoy", Modifier.weight(1f))
            ContadorCard(proceso.toString(), "En proceso", Modifier.weight(1f))
            ContadorCard(recoger.toString(), "Por recoger", Modifier.weight(1f))
        }
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text("Ingresos de la semana", color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(String.format(Locale.US, "S/ %.2f", ingresos), color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall)
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Pedidos por estado", style = MaterialTheme.typography.titleMedium)
                GraficoDona(porEstado)
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Ingresos por servicio (S/)", style = MaterialTheme.typography.titleMedium)
                GraficoBarras(porServicio)
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text("$recoger pedidos esperan recojo", style = MaterialTheme.typography.titleSmall)
                    if (atrasados > 0) Text("$atrasados pedidos atrasados", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onVerPedidos, enabled = recoger > 0) { Text("Ver") }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ContadorCard(valor: String, titulo: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(Modifier.padding(12.dp)) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelSmall)
        }
    }
}
