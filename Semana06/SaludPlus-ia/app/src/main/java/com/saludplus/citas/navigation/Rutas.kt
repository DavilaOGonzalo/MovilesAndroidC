package com.saludplus.citas.navigation

import android.net.Uri

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val TERMINOS = "terminos"
    const val HOME = "home"
    const val CITAS = "citas"
    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fechaHora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmarCita/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "citaExitosa"

    fun medicos(especialidadId: Int): String = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int): String = "fechaHora/$medicoId"

    fun confirmarCita(medicoId: Int, fecha: String, hora: String): String =
        "confirmarCita/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
}
