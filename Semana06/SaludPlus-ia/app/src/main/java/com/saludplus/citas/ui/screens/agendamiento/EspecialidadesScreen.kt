package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.components.EncabezadoConVolver
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.components.TarjetaSeccion
import com.saludplus.citas.ui.components.estiloEspecialidad

@Composable
fun EspecialidadesScreen(
    onSeleccionar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val especialidades = Repositorio.buscarEspecialidades(texto)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        EncabezadoConVolver(titulo = "Especialidades", onVolver = onVolver)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Elige la especialidad que necesitas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        CampoTexto(
            valor = texto,
            onValorChange = { texto = it },
            etiqueta = "Buscar especialidad",
            icono = Icons.Filled.Search
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (especialidades.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.Search,
                texto = "No se encontraron especialidades"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(especialidades) { especialidad ->
                    val estilo = estiloEspecialidad(especialidad.id)
                    TarjetaSeccion(
                        titulo = especialidad.nombre,
                        descripcion = especialidad.descripcion,
                        onClick = { onSeleccionar(especialidad.id) },
                        icono = estilo.icono,
                        flecha = true,
                        colorIcono = estilo.color
                    )
                }
            }
        }
    }
}
