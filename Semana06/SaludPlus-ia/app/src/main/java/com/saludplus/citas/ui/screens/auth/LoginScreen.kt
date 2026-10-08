package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.components.LogoSaludPlus

@Composable
fun LoginScreen(
    onLoginCorrecto: () -> Unit,
    onIrRegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var mensajeGeneral by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(315.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEAF5FF), MaterialTheme.colorScheme.surface)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoSaludPlus(tamano = 70.dp, mostrarNombre = false)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Clínica SaludPlus",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tu salud, nuestra prioridad",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AsyncImage(
                model = Repositorio.medicos.firstOrNull()?.fotoUrl,
                contentDescription = "Profesional de SaludPlus",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = 190.dp, height = 150.dp)
                    .clip(RoundedCornerShape(topStart = 92.dp, topEnd = 92.dp))
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CampoTexto(
                valor = email,
                onValorChange = {
                    email = it
                    errorEmail = null
                    mensajeGeneral = null
                },
                etiqueta = "Correo electrónico",
                tipoTeclado = KeyboardType.Email,
                error = errorEmail,
                icono = Icons.Filled.Email
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoTexto(
                valor = password,
                onValorChange = {
                    password = it
                    errorPassword = null
                    mensajeGeneral = null
                },
                etiqueta = "Contraseña",
                esPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible },
                error = errorPassword,
                icono = Icons.Filled.Lock
            )

            TextButton(onClick = {}) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            mensajeGeneral?.let {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            BotonGradiente(
                texto = "Iniciar sesión",
                onClick = {
                    errorEmail = when {
                        email.isBlank() -> "Ingrese su email"
                        !email.contains("@") -> "Email inválido"
                        else -> null
                    }
                    errorPassword = if (password.isBlank()) {
                        "Ingrese su contraseña"
                    } else {
                        null
                    }
                    if (errorEmail == null && errorPassword == null) {
                        if (Repositorio.iniciarSesion(email, password)) {
                            onLoginCorrecto()
                        } else {
                            mensajeGeneral = "Email o contraseña incorrectos"
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("o", style = MaterialTheme.typography.bodySmall)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(14.dp))
            BotonSecundario(texto = "Crear cuenta", onClick = onIrRegistro)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
