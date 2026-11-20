package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.theme.CentroTareasTheme
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun PantallaTareas() {
    val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory(TareasRepositoryLocal()))
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    var toques by remember { mutableIntStateOf(0) }
    CentroTareasTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("CentroTareas") }) }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card { Column(Modifier.padding(16.dp)) { Text("Tarea con progreso", style = MaterialTheme.typography.titleMedium); Button(onClick = { toques++ }) { Text("Tócame: $toques") }; Button(onClick = { vm.tareaLarga() }) { Text("Iniciar") }; LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth()); Text("${(progreso * 100).toInt()} %"); Text(mensaje) } }
                Card { Column(Modifier.padding(16.dp)) { Text("Contar números primos", style = MaterialTheme.typography.titleMedium); OutlinedTextField("1000", {}, label = { Text("Hasta N") }); Button(onClick = {}) { Text("Contar") } } }
                Card { Column(Modifier.padding(16.dp)) { Text("Dos cargas", style = MaterialTheme.typography.titleMedium); Row { OutlinedButton(onClick = {}) { Text("Una por una") }; Button(onClick = {}) { Text("A la vez") } } } }
            }
        }
    }
}






