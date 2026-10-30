package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.theme.LavaTheme
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

    LavaTheme(rol = rol) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconoServicio("wash", Modifier.size(68.dp))
            Spacer(Modifier.height(18.dp))
            Text("Bienvenido", style = MaterialTheme.typography.headlineLarge)
            Text("Ingresa para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(13.dp)).padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SelectorRol("Estudiante", rol == Rol.CLIENTE.name, { rol = Rol.CLIENTE.name }, Modifier.weight(1f))
                SelectorRol("Administrador", rol == Rol.ADMIN.name, { rol = Rol.ADMIN.name }, Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            LavaTextField(
                value = correo,
                onValueChange = { correo = it; vm.limpiarError() },
                label = "Correo",
                supportingText = if (rol == Rol.CLIENTE.name) "Ejemplo: ana@correo.test" else "Ejemplo: admin@lavacampus.test",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = Icons.Outlined.Email
            )
            Spacer(Modifier.height(10.dp))
            LavaTextField(
                value = clave,
                onValueChange = { clave = it; vm.limpiarError() },
                label = "Contraseña",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = Icons.Outlined.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(18.dp))
            LavaButton(text = "Ingresar", onClick = { vm.login(correo, clave, rol) })
            Spacer(Modifier.height(14.dp))
            Text("Modo práctica local", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
            Text("Puedes cambiar de perfil desde Ajustes", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SelectorRol(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    ) {
        Text(
            label,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
