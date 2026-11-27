package com.davila.avisoscampus.notificacion

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.davila.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
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
