package com.gonzalo.gymtecsup_mio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkDark
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGreen
import com.gonzalo.gymtecsup_mio.ui.theme.WhiteSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = WhiteSurface,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .background(LightGreen, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "DR",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Diego Ramos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = InkDark,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Plan Premium",
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkGray,
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ProfileStatCard(
                        value = "14",
                        label = "Clases",
                        modifier = Modifier.weight(1f),
                    )
                    ProfileStatCard(
                        value = "3",
                        label = "Rachas",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item {
                Text(
                    text = "Información",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileInfoRow(label = "Sede", value = "TECSUP Lima")
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileInfoRow(label = "Membresía", value = "Premium")
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        ProfileInfoRow(label = "Miembro desde", value = "Marzo 2026")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = InkDark,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = InkGray,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = InkGray,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}