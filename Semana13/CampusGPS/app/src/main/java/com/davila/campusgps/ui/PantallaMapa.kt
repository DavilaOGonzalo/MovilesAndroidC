package com.davila.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        ) {
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

            when (val ui = estado) {
                UbicacionUi.Inicial -> Text("Solicita permiso para consultar tu ubicación.")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                UbicacionUi.PermisoDenegado -> Text(
                    text = "Sin permiso de ubicación no se puede mostrar dónde estás.",
                    color = MaterialTheme.colorScheme.error,
                )
                is UbicacionUi.Disponible -> Text(
                    "Latitud: ${ui.posicion.latitud}\nLongitud: ${ui.posicion.longitud}",
                )
                is UbicacionUi.Error -> Text(
                    text = ui.mensaje,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}