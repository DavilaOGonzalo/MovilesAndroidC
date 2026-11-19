package com.davila.campusgps.model

data class Posicion(
    val latitud: Double,
    val longitud: Double,
)

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double,
)

data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float?,
)

sealed interface UbicacionUi {
    data object Inicial : UbicacionUi
    data object Buscando : UbicacionUi
    data object PermisoDenegado : UbicacionUi
    data class Disponible(val posicion: Posicion) : UbicacionUi
    data class Error(val tipo: TipoErrorUbicacion, val mensaje: String) : UbicacionUi
}

enum class TipoErrorUbicacion {
    GPS_APAGADO,
    SIN_PERMISO,
    SIN_PLAY_SERVICES,
    OTRO,
}

class ErrorUbicacion(
    val tipo: TipoErrorUbicacion,
    mensaje: String,
) : Exception(mensaje)
