package com.gonzalo.gymtecsup_mio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gonzalo.gymtecsup_mio.data.EstadoReserva
import com.gonzalo.gymtecsup_mio.data.Reserva
import com.gonzalo.gymtecsup_mio.data.ReservaCatalog
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkDark
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisReservasScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis reservas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                ),
            )
        },
    ) { innerPadding ->
        if (ReservaCatalog.reservas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Aún no tienes reservas.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkGray,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(ReservaCatalog.reservas, key = { it.id }) { reserva ->
                    ReservaCard(reserva = reserva)
                }
            }
        }
    }
}

@Composable
private fun ReservaCard(reserva: Reserva) {
    val confirmada = reserva.estado == EstadoReserva.CONFIRMADA

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(if (confirmada) DarkGreen else Color.Transparent),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 14.dp),
            ) {
                Text(
                    text = reserva.className,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reserva.schedule,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkGray,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reserva.instructor,
                    style = MaterialTheme.typography.bodySmall,
                    color = InkGray,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                EstadoBadge(estado = reserva.estado)
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: EstadoReserva) {
    val (background, content, label) = when (estado) {
        EstadoReserva.CONFIRMADA -> Triple(LightGreen, DarkGreen, "Confirmada")
        EstadoReserva.COMPLETADA -> Triple(Color(0xFFE6E8EA), InkGray, "Completada")
    }

    Box(
        modifier = Modifier
            .padding(top = 14.dp, end = 14.dp)
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = FontWeight.Medium,
        )
    }
}