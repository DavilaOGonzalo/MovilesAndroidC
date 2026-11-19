package com.davila.campusgps.util

import android.location.Location
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.Posicion

fun metrosDesde(yo: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        yo.latitud,
        yo.longitud,
        hasta.latitud,
        hasta.longitud,
        resultado,
    )
    return resultado[0]
}
