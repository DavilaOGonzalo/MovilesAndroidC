package com.tecsup.murocampus.viewmodel

import com.tecsup.murocampus.data.ErrorDeDatos
import java.io.IOException

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun mensajeDe(error: Throwable): String = when (error) {
    is ErrorDeDatos -> error.message ?: "Error desconocido"
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    else -> "Error desconocido"
}
