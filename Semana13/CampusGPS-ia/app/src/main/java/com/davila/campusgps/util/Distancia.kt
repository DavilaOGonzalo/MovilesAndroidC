package com.davila.campusgps.util

import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.Posicion
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun metrosDesde(yo: Posicion, hasta: Lugar): Float? {
    val destino = hasta.posicion ?: return null
    val latitudOrigen = Math.toRadians(yo.latitud)
    val latitudDestino = Math.toRadians(destino.latitud)
    val diferenciaLatitud = latitudDestino - latitudOrigen
    val diferenciaLongitud = Math.toRadians(destino.longitud - yo.longitud)

    val a = (
        sin(diferenciaLatitud / 2) * sin(diferenciaLatitud / 2) +
            cos(latitudOrigen) * cos(latitudDestino) *
            sin(diferenciaLongitud / 2) * sin(diferenciaLongitud / 2)
        ).coerceIn(0.0, 1.0)
    val distanciaMetros = 2 * RADIO_TERRESTRE_METROS * atan2(sqrt(a), sqrt(1 - a))
    return distanciaMetros.toFloat()
}

private const val RADIO_TERRESTRE_METROS = 6_371_000.0
