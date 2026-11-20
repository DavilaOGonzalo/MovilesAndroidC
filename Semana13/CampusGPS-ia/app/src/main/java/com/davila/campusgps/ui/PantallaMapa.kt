package com.davila.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.TipoErrorUbicacion
import com.davila.campusgps.model.UbicacionUi
import com.davila.campusgps.util.formatearDistancia
import com.davila.campusgps.viewmodel.MapaViewModel

private val permisosUbicacion = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(viewModel: MapaViewModel) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()
    val lugaresParaMapa by viewModel.lugaresParaMapa.collectAsState()
    val lugaresConDistancia by viewModel.lugaresConDistancia.collectAsState()
    val seleccionadoId by viewModel.seleccionadoId.collectAsState()
    val lugarSeleccionado by viewModel.lugarSeleccionado.collectAsState()
    val lugarMasCercano by viewModel.lugarMasCercano.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val radioMetros by viewModel.radioMetros.collectAsState()
    val errorPersistencia by viewModel.errorPersistencia.collectAsState()
    val posicionUsuario = (estado as? UbicacionUi.Disponible)?.posicion

    val solicitudPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permisosConcedidos ->
        val permisoOtorgado = permisosConcedidos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisosConcedidos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (permisoOtorgado) viewModel.buscar() else viewModel.permisoDenegado()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("CampusGPS") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            MapaUbicacion(
                posicionUsuario = posicionUsuario,
                lugares = lugaresParaMapa,
                lugarSeleccionado = lugarSeleccionado,
                lugarMasCercanoId = lugarMasCercano?.lugar?.id,
                radioMetros = radioMetros,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.1f),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CategoriaFiltro.entries.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { viewModel.seleccionarCategoria(categoria) },
                        label = { Text(categoria.etiqueta) },
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Radio:", modifier = Modifier.padding(end = 8.dp))
                MapaViewModel.RADIOS_CERCANIA_METROS.forEach { radio ->
                    FilterChip(
                        selected = radioMetros == radio,
                        onClick = { viewModel.seleccionarRadio(radio) },
                        label = { Text("$radio m") },
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }

            Text(
                text = if (posicionUsuario == null) {
                    "Obtén tu ubicación para filtrar por radio."
                } else {
                    "${lugaresConDistancia.size} lugares dentro de $radioMetros m"
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 2.dp),
            )

            Button(
                enabled = estado !is UbicacionUi.Buscando,
                onClick = {
                    val tienePermisoPreciso = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED
                    val tienePermisoAproximado = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED

                    if (tienePermisoPreciso || tienePermisoAproximado) {
                        viewModel.buscar()
                    } else {
                        solicitudPermisos.launch(permisosUbicacion)
                    }
                },
            ) {
                Text(if (estado is UbicacionUi.Buscando) "Buscando ubicación…" else "Obtener mi ubicación")
            }

            Button(
                enabled = lugarMasCercano != null,
                onClick = viewModel::irAlMasCercano,
            ) {
                Text("Ir al más cercano")
            }

            lugarMasCercano?.let { cercano ->
                Text(
                    text = "Más cercano: ${cercano.lugar.nombre} (${formatearDistancia(cercano.metros)})",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp),
                )
            }

            if (errorPersistencia) {
                Text(
                    text = "No se pudo guardar el último lugar visitado.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            when (val ui = estado) {
                UbicacionUi.Inicial -> Text(
                    text = "Solicita permiso para consultar tu ubicación.",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                )
                UbicacionUi.Buscando -> CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                UbicacionUi.PermisoDenegado -> MensajeErrorUbicacion(
                    mensaje = "Sin permiso de ubicación no se puede mostrar dónde estás.",
                    tipo = TipoErrorUbicacion.SIN_PERMISO,
                    onReintentar = { solicitudPermisos.launch(permisosUbicacion) },
                )
                is UbicacionUi.Disponible -> Text(
                    text = "Latitud: ${ui.posicion.latitud}\nLongitud: ${ui.posicion.longitud}",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
                is UbicacionUi.Error -> MensajeErrorUbicacion(
                    mensaje = ui.mensaje,
                    tipo = ui.tipo,
                    onReintentar = viewModel::buscar,
                )
            }

            ListaLugares(
                lugares = lugaresConDistancia,
                seleccionadoId = seleccionadoId,
                radioMetros = radioMetros,
                onSeleccionar = viewModel::seleccionar,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
            )
        }
    }
}
