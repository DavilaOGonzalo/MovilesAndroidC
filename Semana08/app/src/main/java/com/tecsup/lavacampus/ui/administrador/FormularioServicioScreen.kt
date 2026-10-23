package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.FormularioServicioViewModel

@Composable
fun FormularioServicioScreen(
    vm: FormularioServicioViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val nombre by vm.nombre.collectAsStateWithLifecycle()
    val descripcion by vm.descripcion.collectAsStateWithLifecycle()
    val unidad by vm.unidad.collectAsStateWithLifecycle()
    val precio by vm.precio.collectAsStateWithLifecycle()
    val horas by vm.horas.collectAsStateWithLifecycle()
    val icono by vm.icono.collectAsStateWithLifecycle()
    val activo by vm.activo.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTopBar(title = "${if (nombre.isBlank()) "Nuevo" else "Editar"} servicio", onBack = onBack)
        LavaTextField(nombre, vm::cambiarNombre, "Nombre")
        LavaTextField(descripcion, vm::cambiarDescripcion, "Descripción", singleLine = false)
        Text("Se cobra por", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("KG", "PRENDA", "UNIDAD").forEach { opcion ->
                FilterChip(selected = unidad == opcion, onClick = { vm.cambiarUnidad(opcion) }, label = { Text(opcion) })
            }
        }
        LavaTextField(precio, vm::cambiarPrecio, "Precio")
        Text("Tiempo de entrega", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(24, 48, 72).forEach { opcion ->
                FilterChip(selected = horas == opcion, onClick = { vm.cambiarHoras(opcion) }, label = { Text("$opcion h") })
            }
        }
        Text("Ícono", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("wash", "iron", "shirt").forEach { opcion ->
                FilterChip(selected = icono == opcion, onClick = { vm.cambiarIcono(opcion) }, label = { IconoServicio(opcion) })
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Servicio activo", modifier = Modifier.padding(top = 12.dp))
            Switch(checked = activo, onCheckedChange = vm::cambiarActivo)
        }
        if (error != null) Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
        LavaButton(text = "Guardar servicio", onClick = { vm.guardar(onSaved) })
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
        Spacer(Modifier.height(12.dp))
    }
}
