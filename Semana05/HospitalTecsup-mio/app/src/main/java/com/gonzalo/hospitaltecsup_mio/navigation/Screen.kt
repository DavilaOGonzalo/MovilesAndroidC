package com.gonzalo.hospitaltecsup_mio.navigation

import android.net.Uri

// Contrato central de navegación de Clínica Salud+.
sealed class Screen(val route: String) {

    // Pantalla de inicio
    object Home : Screen("home")

    // Perfil del médico seleccionado — recibe los datos del médico por navegación.
    object DoctorProfile : Screen("doctor/{doctorId}/{name}/{specialty}/{rating}") {
        fun createRoute(
            doctorId: Int,
            name: String,
            specialty: String,
            rating: Float,
        ): String = "doctor/$doctorId/${Uri.encode(name)}/${Uri.encode(specialty)}/$rating"
    }

    // Agenda de la cita
    object ScheduleAppointment : Screen("schedule/{doctorId}") {
        fun createRoute(doctorId: Int): String = "schedule/$doctorId"
    }

    // Confirmación final de la cita
    object Confirmation : Screen("confirmation/{doctorId}/{date}/{time}") {
        fun createRoute(doctorId: Int, date: String, time: String): String =
            "confirmation/$doctorId/$date/$time"
    }
}