package com.davila.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.Posicion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapaUbicacion(
    posicionUsuario: Posicion?,
    lugares: List<Lugar>,
    modifier: Modifier = Modifier,
) {
    val centroInicial = remember { LatLng(-12.0464, -77.0428) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centroInicial, 16f)
    }

    LaunchedEffect(posicionUsuario) {
        posicionUsuario?.let { posicion ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(posicion.latitud, posicion.longitud),
                    17f,
                ),
                durationMs = 800,
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        lugares.forEach { lugar ->
            val coordenada = LatLng(lugar.latitud, lugar.longitud)
            Marker(
                state = rememberMarkerState(position = coordenada),
                title = lugar.nombre,
                snippet = lugar.categoria,
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE,
                ),
            )
        }

        posicionUsuario?.let { posicion ->
            val coordenada = LatLng(posicion.latitud, posicion.longitud)
            Marker(
                state = rememberMarkerState(position = coordenada),
                title = "Estás aquí",
                snippet = "Tu ubicación actual",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
            )
        }
    }
}
