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
    data class Error(val mensaje: String) : UbicacionUi
}
