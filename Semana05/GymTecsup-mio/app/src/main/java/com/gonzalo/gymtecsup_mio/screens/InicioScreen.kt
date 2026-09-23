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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.gonzalo.gymtecsup_mio.data.ClassCatalog
import com.gonzalo.gymtecsup_mio.data.FitnessClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TECSUP Fit",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column {
                    Text(
                        text = "Bienvenido a tu gimnasio",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Encuentra la clase perfecta para ti y mejora tu entrenamiento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item { StatsRow() }

            item {
                Text(
                    text = "Clases disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            items(ClassCatalog.classes, key = { it.id }) { fitnessClass ->
                FitnessClassCard(fitnessClass = fitnessClass)
            }
        }
    }
}

@Composable
private fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            value = "${ClassCatalog.classes.size}",
            label = "Clases",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            value = "${ClassCatalog.classes.map { it.instructor }.distinct().size}",
            label = "Instructores",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            value = "${ClassCatalog.classes.map { it.category }.distinct().size}",
            label = "Categorías",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun FitnessClassCard(fitnessClass: FitnessClass) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = fitnessClass.category.take(1),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fitnessClass.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${fitnessClass.instructor} · ${fitnessClass.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ClassInfoChip(text = fitnessClass.duration)
            ClassInfoChip(text = fitnessClass.schedule)
            IntensityBadge(intensity = fitnessClass.intensity)
        }
    }
}

@Composable
private fun ClassInfoChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun IntensityBadge(intensity: String) {
    val background = when (intensity) {
        "Alta" -> Color(0xFFFFEBEE)
        "Media" -> Color(0xFFFFF3E0)
        else -> Color(0xFFE8F5E9)
    }
    val content = when (intensity) {
        "Alta" -> Color(0xFFC62828)
        "Media" -> Color(0xFFE65100)
        else -> Color(0xFF2E7D32)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = "Intensidad $intensity",
            style = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = FontWeight.Medium,
        )
    }
}