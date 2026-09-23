package com.gonzalo.hospitaltecsup_mio.navigation

// Contrato central de navegación de Clínica Salud+.
sealed class Screen(val route: String) {

    // Pantalla de inicio
    object Home : Screen("home")

    // Perfil del médico seleccionado
    object DoctorProfile : Screen("doctor/{doctorId}") {
        fun createRoute(doctorId: Int): String = "doctor/$doctorId"
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