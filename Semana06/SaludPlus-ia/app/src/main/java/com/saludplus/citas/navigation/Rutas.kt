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
    const val FICHA_MEDICO = "fichaMedico/{medicoId}"
    const val FECHA_HORA = "fechaHora/{medicoId}"
    const val MOTIVO_LUGAR = "motivoLugar/{medicoId}/{fecha}/{hora}"
    const val CONFIRMAR_CITA =
        "confirmarCita/{medicoId}/{fecha}/{hora}?motivo={motivo}&lugar={lugar}"
    const val CITA_EXITOSA = "citaExitosa"

    fun medicos(especialidadId: Int): String = "medicos/$especialidadId"
    fun fichaMedico(medicoId: Int): String = "fichaMedico/$medicoId"
    fun fechaHora(medicoId: Int): String = "fechaHora/$medicoId"
    fun motivoLugar(medicoId: Int, fecha: String, hora: String): String =
        "motivoLugar/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"

    fun confirmarCita(
        medicoId: Int,
        fecha: String,
        hora: String,
        motivo: String = "Control médico",
        lugar: String = "Clínica SaludPlus - Sede Principal"
    ): String =
        "confirmarCita/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}" +
                "?motivo=${Uri.encode(motivo)}&lugar=${Uri.encode(lugar)}"
}
