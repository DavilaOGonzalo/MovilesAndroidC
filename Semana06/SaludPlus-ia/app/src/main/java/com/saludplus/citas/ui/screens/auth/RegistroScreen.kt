package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.components.gradienteMarca

@Composable
fun RegistroScreen(
    onRegistroCorrecto: () -> Unit,
    onIrTerminos: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }
    var mensajeGeneral by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(gradienteMarca())
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Regístrate para empezar a agendar citas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = nombre,
                onValorChange = { nombre = it; errorNombre = null; mensajeGeneral = null },
                etiqueta = "Nombre completo",
                error = errorNombre,
                icono = Icons.Filled.Person
            )
            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(
                valor = email,
                onValorChange = { email = it; errorEmail = null; mensajeGeneral = null },
                etiqueta = "Email",
                tipoTeclado = KeyboardType.Email,
                error = errorEmail,
                icono = Icons.Filled.Email
            )
            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(
                valor = password,
                onValorChange = { password = it; errorPassword = null; mensajeGeneral = null },
                etiqueta = "Contraseña",
                esPassword = true,
                error = errorPassword,
                icono = Icons.Filled.Lock
            )
            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(
                valor = confirmarPassword,
                onValorChange = { confirmarPassword = it; errorConfirmar = null; mensajeGeneral = null },
                etiqueta = "Confirmar contraseña",
                esPassword = true,
                error = errorConfirmar,
                icono = Icons.Filled.Lock
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onIrTerminos) {
                Text("Ver términos y condiciones")
            }

            mensajeGeneral?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BotonGradiente(
                texto = "Registrarme",
                onClick = {
                    errorNombre = if (nombre.isBlank()) "Ingrese su nombre" else null
                    errorEmail = when {
                        email.isBlank() -> "Ingrese su email"
                        !email.contains("@") -> "Email inválido"
                        else -> null
                    }
                    errorPassword = when {
                        password.isBlank() -> "Ingrese su contraseña"
                        password.length < 6 -> "Mínimo 6 caracteres"
                        else -> null
                    }
                    errorConfirmar = when {
                        confirmarPassword.isBlank() -> "Confirme su contraseña"
                        confirmarPassword != password -> "Las contraseñas no coinciden"
                        else -> null
                    }

                    val esValido = errorNombre == null && errorEmail == null &&
                            errorPassword == null && errorConfirmar == null

                    if (esValido) {
                        val registrado = Repositorio.registrarUsuario(nombre, email, password)
                        if (registrado) {
                            onRegistroCorrecto()
                        } else {
                            mensajeGeneral = "El email ya está registrado"
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onRegistroCorrecto) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}
