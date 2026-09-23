package com.gonzalo.gymtecsup_mio.screens

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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gonzalo.gymtecsup_mio.data.ClassCatalog
import com.gonzalo.gymtecsup_mio.data.ReservaCatalog
import com.gonzalo.gymtecsup_mio.navigation.Screen
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkDark
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGreen

@Composable
fun ConfirmationScreen(
    navController: NavController,
    classId: Int,
) {
    val fitnessClass = remember(classId) { ClassCatalog.classes.find { it.id == classId } }

    LaunchedEffect(classId, fitnessClass) {
        fitnessClass?.let { ReservaCatalog.add(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(92.dp)
                .background(LightGreen, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = DarkGreen,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "¡Cupo reservado!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = InkDark,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tu cupo fue reservado con éxito.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkGray,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = fitnessClass?.name.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = InkDark,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = fitnessClass?.schedule.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = InkGray,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = fitnessClass?.room.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = InkGray,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(44.dp))

        Button(
            onClick = {
                navController.navigate(Screen.RESERVAS) {
                    popUpTo(Screen.HOME) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGray,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        ) {
            Text(
                text = "Ver mis reservas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = InkDark,
            )
        }
    }
}