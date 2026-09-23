package com.gonzalo.gymtecsup_mio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gonzalo.gymtecsup_mio.data.ClassCatalog
import com.gonzalo.gymtecsup_mio.data.FitnessClass
import com.gonzalo.gymtecsup_mio.navigation.Screen
import com.gonzalo.gymtecsup_mio.ui.theme.DarkGreen
import com.gonzalo.gymtecsup_mio.ui.theme.InkDark
import com.gonzalo.gymtecsup_mio.ui.theme.InkGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGray
import com.gonzalo.gymtecsup_mio.ui.theme.LightGreen
import java.util.Calendar

@Composable
fun InicioScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf(ClassFilter.TODAY) }

    val filteredClasses = remember(selectedFilter) {
        when (selectedFilter) {
            ClassFilter.TODAY -> ClassCatalog.classes.filter { it.days.contains(todayName()) }
            ClassFilter.THIS_WEEK -> ClassCatalog.classes
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        HomeHeader()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { StatsRow() }

            item {
                Text(
                    text = "Clases disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )
            }

            item {
                ClassFilterRow(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                )
            }

            items(filteredClasses, key = { it.id }) { fitnessClass ->
                FitnessClassCard(
                    fitnessClass = fitnessClass,
                    onClick = {
                        navController.navigate(Screen.classDetail(fitnessClass.id))
                    },
                )
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkGreen,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            )
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp, bottom = 22.dp),
    ) {
        Text(
            text = "TECSUP Fit",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Hola, Diego",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

private enum class ClassFilter(val label: String) {
    TODAY("Hoy"),
    THIS_WEEK("Esta semana"),
}

private fun todayName(): String = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
    Calendar.MONDAY -> "Lunes"
    Calendar.TUESDAY -> "Martes"
    Calendar.WEDNESDAY -> "Miércoles"
    Calendar.THURSDAY -> "Jueves"
    Calendar.FRIDAY -> "Viernes"
    Calendar.SATURDAY -> "Sábado"
    else -> "Domingo"
}

@Composable
private fun ClassFilterRow(
    selectedFilter: ClassFilter,
    onFilterSelected: (ClassFilter) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ClassFilter.entries.forEach { filter ->
            val selected = selectedFilter == filter
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) DarkGreen else LightGray)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
            ) {
                Text(
                    text = filter.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else InkGray,
                )
            }
        }
    }
}

@Composable
private fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
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
        colors = CardDefaults.cardColors(containerColor = LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
                color = InkDark,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = InkGray,
            )
        }
    }
}

@Composable
private fun FitnessClassCard(
    fitnessClass: FitnessClass,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightGreen),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.FitnessCenter,
                    contentDescription = null,
                    tint = DarkGreen,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fitnessClass.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = InkDark,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = fitnessClass.schedule,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkGray,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = fitnessClass.room,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkGray,
                )
            }
        }
    }
}