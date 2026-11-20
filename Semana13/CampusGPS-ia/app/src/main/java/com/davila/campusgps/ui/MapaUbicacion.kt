package com.davila.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.Posicion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapaUbicacion(
    posicionUsuario: Posicion?,
    lugares: List<LugarConDistancia>,
    seleccionadoId: Int?,
    lugarMasCercanoId: Int?,
    radioMetros: Int,
    modifier: Modifier = Modifier,
) {
    // Centro de Tecsup Lima según el polígono de OpenStreetMap.
    val centroInicial = remember { LatLng(-12.04398, -76.95291) }
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

    val lugarSeleccionado = lugares.firstOrNull { it.lugar.id == seleccionadoId }?.lugar
    LaunchedEffect(lugarSeleccionado?.id) {
        lugarSeleccionado?.posicion?.let { posicion ->
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
        posicionUsuario?.let { posicion ->
            Circle(
                center = LatLng(posicion.latitud, posicion.longitud),
                radius = radioMetros.toDouble(),
                fillColor = Color(0x223F51B5),
                strokeColor = Color(0xFF3F51B5),
                strokeWidth = 2f,
            )
        }
        lugares.forEach { lugar ->
            val posicion = lugar.lugar.posicion ?: return@forEach
            val coordenada = LatLng(posicion.latitud, posicion.longitud)
            val estadoRadio = when (lugar.dentroDelRadio) {
                true -> "Dentro de $radioMetros m"
                false -> "Fuera de $radioMetros m"
                null -> "Radio sin ubicación actual"
            }
            key(lugar.lugar.id) {
                Marker(
                    state = rememberMarkerState(position = coordenada),
                    title = lugar.lugar.nombre,
                    snippet = "${lugar.lugar.categoria} · $estadoRadio",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.lugar.id == lugarMasCercanoId) {
                            BitmapDescriptorFactory.HUE_GREEN
                        } else {
                            BitmapDescriptorFactory.HUE_AZURE
                        },
                    ),
                )
            }
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
