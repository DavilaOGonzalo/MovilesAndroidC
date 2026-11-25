package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.R
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaEditar(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val detalle by vm.detalle.collectAsStateWithLifecycle()
    val edicion by vm.edicion.collectAsStateWithLifecycle()
    val post = (detalle as? UiState.Exito)?.datos?.post ?: return
    var titulo by rememberSaveable(post.id) { mutableStateOf(post.title) }
    val guardando = edicion == UiState.Cargando
    val foco = LocalFocusManager.current

    LaunchedEffect(edicion) {
        val respuesta = edicion as? UiState.Exito ?: return@LaunchedEffect
        titulo = respuesta.datos.title
        foco.clearFocus()
    }

    Column(
        modifier = modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(R.string.editar_titulo), style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                vm.limpiarAvisoEdicion()
            },
            label = { Text(stringResource(R.string.titulo_publicacion)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !guardando,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { vm.editarTitulo(titulo) },
                enabled = !guardando && titulo.isNotBlank() && titulo.trim() != post.title,
            ) { Text(stringResource(R.string.guardar_titulo)) }
            TextButton(onClick = vm::cerrarEditor, enabled = !guardando) {
                Text(stringResource(R.string.volver_al_detalle))
            }
        }
        when (val actual = edicion) {
            null -> Unit
            UiState.Cargando -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Exito -> Text(
                stringResource(R.string.titulo_actualizado, actual.datos.title),
                color = MaterialTheme.colorScheme.primary,
            )
            is UiState.Error -> Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
