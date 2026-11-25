package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }
    val estado by vm.publicacion.collectAsStateWithLifecycle()
    val publicando = estado == UiState.Cargando
    val foco = LocalFocusManager.current

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            texto = ""
            foco.clearFocus()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                if (estado is UiState.Exito) vm.limpiarPublicacion()
                titulo = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.titulo_publicacion)) },
            singleLine = true,
            enabled = !publicando,
        )
        OutlinedTextField(
            value = texto,
            onValueChange = {
                if (estado is UiState.Exito) vm.limpiarPublicacion()
                texto = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.contenido_publicacion)) },
            minLines = 4,
            enabled = !publicando,
        )
        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = !publicando && titulo.isNotBlank() && texto.isNotBlank(),
        ) {
            Text(stringResource(R.string.publicar))
        }
        when (val actual = estado) {
            null -> Unit
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Text(
                stringResource(R.string.publicado_con_id, actual.datos.id),
                color = MaterialTheme.colorScheme.primary,
            )
            is UiState.Error -> Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
