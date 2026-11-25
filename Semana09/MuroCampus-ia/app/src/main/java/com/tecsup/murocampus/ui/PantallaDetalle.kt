package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.R
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(
                mensaje = actual.mensaje,
                alReintentar = vm::reintentarDetalle,
                modifier = Modifier.padding(16.dp),
            )
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(actual.datos.post.title, style = MaterialTheme.typography.headlineSmall)
                        Text(actual.datos.post.body)
                    }
                }
                item {
                    Text(
                        stringResource(R.string.comentarios_cantidad, actual.datos.comentarios.size),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                items(actual.datos.comentarios, key = { it.id }) { comentario ->
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            comentario.email,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(comentario.body)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
