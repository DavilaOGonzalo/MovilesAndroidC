package com.davila.campusgps.model

data class Posicion(
    val latitud: Double,
    val longitud: Double,
)

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val posicion: Posicion?,
    val referenciaPendiente: String? = null,
)

data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float?,
)

enum class CategoriaFiltro(val etiqueta: String, val categoria: String?) {
    TODOS("Todos", null),
    ESTUDIO("Estudio", "Estudio"),
    COMIDA("Comida", "Comida"),
    EVENTOS("Eventos", "Eventos"),
    SERVICIOS("Servicios", "Servicios"),
}

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
