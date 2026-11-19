package com.davila.campusgps.model

data class Posicion(
    val latitud: Double,
    val longitud: Double,
)

sealed interface UbicacionUi {
    data object Inicial : UbicacionUi
    data object Buscando : UbicacionUi
    data object PermisoDenegado : UbicacionUi
    data class Disponible(val posicion: Posicion) : UbicacionUi
    data class Error(val mensaje: String) : UbicacionUi
}