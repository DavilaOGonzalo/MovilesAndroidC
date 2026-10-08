package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.theme.DisponibleVerde

@Composable
fun CitaExitosaScreen(
    onIrInicio: () -> Unit,
    onVerCitas: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(82.dp)
                    .shadow(6.dp, CircleShape),
                shape = CircleShape,
                color = Color(0xFFE0FAF0),
                contentColor = DisponibleVerde
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.padding(20.dp))
            }
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "¡Cita agendada\ncon éxito!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tu cita médica ha sido registrada.\nTe enviaremos un recordatorio\nantes de la cita.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(42.dp))
            BotonGradiente(
                texto = "Ver mis citas",
                onClick = onVerCitas,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            BotonSecundario(
                texto = "Volver al inicio",
                onClick = onIrInicio,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
