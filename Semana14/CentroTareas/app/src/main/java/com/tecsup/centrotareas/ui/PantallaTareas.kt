package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.ui.theme.CentroTareasTheme
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas() {
    val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory(TareasRepositoryLocal()))
    val mensaje by vm.mensaje.collectAsState(); val progreso by vm.progreso.collectAsState(); val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState(); val carga by vm.carga.collectAsState(); val simularError by vm.simularError.collectAsState()
    val comparaciones by vm.comparaciones.collectAsState()
    var limitePrimos by remember { mutableStateOf("1000") }; var toques by remember { mutableIntStateOf(0) }
    CentroTareasTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("CentroTareas") }) }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Tarea con progreso", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                    Button(onClick = vm::tareaLarga, enabled = !corriendo) { Text("Iniciar") }
                    OutlinedButton(onClick = vm::cancelar, enabled = corriendo) { Text("Cancelar") }
                    LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
                    Text("${(progreso * 100).toInt()} %"); Text(mensaje)
                } }
                Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Contar números primos", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(limitePrimos, { limitePrimos = it.filter(Char::isDigit) }, label = { Text("Hasta N") })
                    Row { Button(onClick = { limitePrimos.toIntOrNull()?.let(vm::contar) }) { Text("Contar") }; OutlinedButton(onClick = vm::cancelarPrimos) { Text("Cancelar") } }
                    when (val estado = primos) { PrimosUi.Inicial -> Text("Ingresa un límite de al menos 2"); PrimosUi.Calculando -> CircularProgressIndicator(); PrimosUi.Cancelado -> Text("Cancelado", color = MaterialTheme.colorScheme.error); is PrimosUi.Listo -> Text("${estado.cantidad} primos · ${estado.milisegundos} ms", color = MaterialTheme.colorScheme.primary) }
                } }
                Card { Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Dos cargas", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) { Text("Simular error"); Spacer(Modifier.width(8.dp)); Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError) }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedButton(onClick = vm::cargarUnaPorUna, enabled = carga !is CargaUi.Cargando) { Text("Una por una") }; Button(onClick = vm::cargarALaVez, enabled = carga !is CargaUi.Cargando) { Text("A la vez") } }
                    when (val estado = carga) { CargaUi.Inicial -> Text("Compara la carga secuencial y paralela"); CargaUi.Cargando -> CircularProgressIndicator(); is CargaUi.Exito -> Text("${estado.modo}: ${estado.perfil}, ${estado.pedidos} pedidos · ${estado.milisegundos} ms", color = MaterialTheme.colorScheme.primary); is CargaUi.Error -> { Text(estado.mensaje, color = MaterialTheme.colorScheme.error); Button(onClick = vm::cargarALaVez) { Text("Reintentar") } } }
                    HorizontalDivider()
                    Text("Comparación de tiempos", style = MaterialTheme.typography.titleSmall)
                    if (comparaciones.isEmpty()) Text("Los resultados se acumularán aquí.") else {
                        comparaciones.forEach { fila ->
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("${fila.modo} · ${fila.milisegundos} ms", style = MaterialTheme.typography.titleSmall)
                                    Text(fila.resultado, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                } }
            }
        }
    }
}

