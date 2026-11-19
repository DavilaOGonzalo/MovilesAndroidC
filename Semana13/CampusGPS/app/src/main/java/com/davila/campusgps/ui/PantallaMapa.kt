package com.davila.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val lugares by viewModel.lugares.collectAsState()
    val lugaresConDistancia by viewModel.lugaresConDistancia.collectAsState()
    val seleccionadoId by viewModel.seleccionadoId.collectAsState()
    val lugarMasCercano by viewModel.lugarMasCercano.collectAsState()
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
                posicionUsuario = (estado as? UbicacionUi.Disponible)?.posicion,
                lugares = lugares,
                seleccionadoId = seleccionadoId,
                lugarMasCercanoId = lugarMasCercano?.lugar?.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.15f),
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
                Text("Obtener mi ubicación")
            }

            Button(
                enabled = lugarMasCercano != null,
                onClick = {
                    lugarMasCercano?.let { viewModel.seleccionar(it.lugar.id) }
                },
            ) {
                Text("Ir al más cercano")
            }

            lugarMasCercano?.let { cercano ->
                Text(
                    text = "Más cercano: ${cercano.lugar.nombre} (${formatearDistancia(cercano.metros)})",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                )
            }

            when (val ui = estado) {
                UbicacionUi.Inicial -> Text(
                    text = "Solicita permiso para consultar tu ubicación.",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
                UbicacionUi.Buscando -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                UbicacionUi.PermisoDenegado -> Text(
                    text = "Sin permiso de ubicación no se puede mostrar dónde estás.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
                is UbicacionUi.Disponible -> Text(
                    text = "Latitud: ${ui.posicion.latitud}\nLongitud: ${ui.posicion.longitud}",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
                is UbicacionUi.Error -> Text(
                    text = ui.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
            }

            ListaLugares(
                lugares = lugaresConDistancia,
                seleccionadoId = seleccionadoId,
                onSeleccionar = viewModel::seleccionar,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}
