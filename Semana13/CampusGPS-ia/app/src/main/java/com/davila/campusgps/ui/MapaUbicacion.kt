package com.davila.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.Posicion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapaUbicacion(
    posicionUsuario: Posicion?,
    lugares: List<LugarConDistancia>,
    lugarSeleccionado: Lugar?,
    lugarMasCercanoId: Int?,
    radioMetros: Int,
    modifier: Modifier = Modifier,
) {
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
            val coordenada = LatLng(posicion.latitud, posicion.longitud)
            Circle(
                center = coordenada,
                radius = radioMetros.toDouble(),
                fillColor = Color(0x223F51B5),
                strokeColor = Color(0xFF3F51B5),
                strokeWidth = 2f,
            )
            key("usuario-${posicion.latitud}-${posicion.longitud}") {
                Marker(
                    state = rememberMarkerState(position = coordenada),
                    title = "Estás aquí",
                    snippet = "Tu ubicación actual",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                )
            }
        }

        lugares.forEach { item ->
            val lugar = item.lugar
            val posicion = lugar.posicion ?: return@forEach
            val coordenada = LatLng(posicion.latitud, posicion.longitud)
            val estadoRadio = when (item.dentroDelRadio) {
                true -> "Dentro de $radioMetros m"
                false -> "Fuera de $radioMetros m"
                null -> "Radio sin ubicación actual"
            }
            key(lugar.id) {
                Marker(
                    state = rememberMarkerState(position = coordenada),
                    title = lugar.nombre,
                    snippet = "${lugar.categoria} · $estadoRadio",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.id == lugarMasCercanoId) {
                            BitmapDescriptorFactory.HUE_GREEN
                        } else {
                            BitmapDescriptorFactory.HUE_AZURE
                        },
                    ),
                )
            }
        }
    }
}
