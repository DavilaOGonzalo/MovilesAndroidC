package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.R
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    alAbrir: (Int) -> Unit,
) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = usuarioFiltro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text(stringResource(R.string.todos_los_usuarios)) },
                )
            }
            items((1..10).toList()) { id ->
                FilterChip(
                    selected = usuarioFiltro == id,
                    onClick = { vm.filtrar(id) },
                    label = { Text(stringResource(R.string.usuario_numero, id)) },
                )
            }
        }
        OutlinedTextField(
            value = busqueda,
            onValueChange = vm::buscar,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            label = { Text(stringResource(R.string.buscar_por_titulo)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    val etiqueta = stringResource(R.string.limpiar_busqueda)
                    IconButton(
                        onClick = {
                            vm.buscar("")
                            foco.clearFocus()
                        },
                        modifier = Modifier.semantics { contentDescription = etiqueta },
                    ) {
                        Text("×", style = MaterialTheme.typography.titleLarge)
                    }
                }
            },
        )
        Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val actual = estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorConReintento(
                    mensaje = actual.mensaje,
                    alReintentar = vm::cargar,
                    modifier = Modifier.padding(16.dp),
                )
                is UiState.Exito -> if (actual.datos.isEmpty()) {
                    Text(
                        stringResource(
                            if (busqueda.isBlank()) R.string.sin_publicaciones
                            else R.string.sin_coincidencias,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(actual.datos, key = { it.id }) { post ->
                            TarjetaPost(
                                userId = post.userId,
                                titulo = post.title,
                                texto = post.body,
                                alAbrir = { alAbrir(post.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}
