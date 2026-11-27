package com.davila.avisoscampus.notificacion

import android.content.Context
import com.davila.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: en Android O+, registrar NotificationChannel(CANAL, "Avisos", IMPORTANCE_DEFAULT).
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: en API < 33 devolver true; en API 33+ consultar POST_NOTIFICATIONS.
        return false
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: comprobar permiso y mostrar con NotificationCompat, usando aviso.id.
        // TODO Paso 4: agregar PendingIntent inmutable hacia MainActivity.
        return false
    }
}
