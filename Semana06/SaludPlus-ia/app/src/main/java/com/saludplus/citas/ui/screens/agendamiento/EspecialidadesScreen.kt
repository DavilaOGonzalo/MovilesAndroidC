package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.saludplus.citas.ui.components.TarjetaSeccion

@Composable
fun EspecialidadesScreen(
    onSeleccionar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val especialidades = Repositorio.buscarEspecialidades(texto)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        EncabezadoConVolver(titulo = "Especialidades", onVolver = onVolver)
        CampoTexto(
            valor = texto,
            onValorChange = { texto = it },
            etiqueta = "Buscar especialidad"
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(especialidades) { especialidad ->
                TarjetaSeccion(
                    titulo = especialidad.nombre,
                    descripcion = especialidad.descripcion,
                    onClick = { onSeleccionar(especialidad.id) }
                )
            }
        }
    }
}
