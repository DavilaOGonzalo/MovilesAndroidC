package com.gonzalo.gymtecsup_mio.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gonzalo.gymtecsup_mio.data.FitnessClass
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkDark
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    fitnessClass: FitnessClass,
    onBack: () -> Unit,
    onReserve: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de clase",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = InkDark,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = InkDark,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = InkDark,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LightGreen),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = DarkGreen,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = fitnessClass.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${fitnessClass.instructor} · ${fitnessClass.intensity}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkGray,
                )

                Spacer(modifier = Modifier.height(22.dp))

                DetailInfoRow(
                    icon = Icons.Outlined.Schedule,
                    text = fitnessClass.schedule,
                )
                Spacer(modifier = Modifier.height(12.dp))
                DetailInfoRow(
                    icon = Icons.Outlined.Place,
                    text = fitnessClass.room,
                )
                Spacer(modifier = Modifier.height(12.dp))
                DetailInfoRow(
                    icon = Icons.Outlined.Timer,
                    text = "${fitnessClass.duration} · ${fitnessClass.intensity}",
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(LightGreen)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = "${fitnessClass.spots} cupos disponibles",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = fitnessClass.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkGray,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                )
            }

            Button(
                onClick = { onReserve() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            ) {
                Text(
                    text = "Reservar cupo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun DetailInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = InkGray,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = InkGray,
        )
    }
}