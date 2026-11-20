package com.davila.campusgps.util

import java.util.Locale

fun formatearDistancia(metros: Float?): String {
    if (metros == null) return "Sin calcular"
    return if (metros < 1_000f) {
        String.format(Locale.US, "%d m", metros.toInt())
    } else {
        String.format(Locale.US, "%.1f km", metros / 1_000f)
    }
}
