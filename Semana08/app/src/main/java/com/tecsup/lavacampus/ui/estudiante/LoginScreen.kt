package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onIngreso: (String) -> Unit
) {
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    LaunchedEffect(usuario) {
        usuario?.let { onIngreso(it.rol) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido", style = MaterialTheme.typography.headlineLarge)
        Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = rol == Rol.CLIENTE.name,
                onClick = { rol = Rol.CLIENTE.name },
                label = { Text("Estudiante") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = rol == Rol.ADMIN.name,
                onClick = { rol = Rol.ADMIN.name },
                label = { Text("Administrador") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(18.dp))
        LavaTextField(
            value = correo,
            onValueChange = { correo = it; vm.limpiarError() },
            label = "Correo",
            supportingText = if (rol == Rol.CLIENTE.name) "Ejemplo: ana@correo.test" else "Ejemplo: admin@lavacampus.test",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))
        LavaTextField(
            value = clave,
            onValueChange = { clave = it; vm.limpiarError() },
            label = "Contraseña",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(22.dp))
        LavaButton(
            text = "Ingresar",
            onClick = { vm.login(correo, clave, rol) }
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Modo práctica local · puedes cambiar de perfil desde Ajustes",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
