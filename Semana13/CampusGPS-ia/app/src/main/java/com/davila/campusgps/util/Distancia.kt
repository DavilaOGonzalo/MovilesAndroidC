package com.davila.campusgps.util

import android.location.Location
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.Posicion

fun metrosDesde(yo: Posicion, hasta: Lugar): Float? {
    val destino = hasta.posicion ?: return null
    val resultado = FloatArray(1)
    Location.distanceBetween(
        yo.latitud,
        yo.longitud,
        destino.latitud,
        destino.longitud,
        resultado,
    )
    return resultado[0]
}
