package com.gonzalo.hospitaltecsup_ia.data

// Estado de una cita de Clínica Salud+.
enum class EstadoCita {
    CONFIRMADA,
    COMPLETADA,
}

// Modelo de datos estático de una cita agendada.
data class Cita(
    val id: Int,
    val doctorId: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val estado: EstadoCita,
)

// Catálogo estático de citas (sin base de datos ni API).
object CitaCatalog {
    val citas = listOf(
        Cita(
            id = 1,
            doctorId = 1,
            doctorName = "Dra. María Fernández",
            specialty = "Cardiología",
            date = "Lun 24",
            time = "9:00 a. m.",
            estado = EstadoCita.CONFIRMADA,
        ),
        Cita(
            id = 2,
            doctorId = 2,
            doctorName = "Dr. Carlos Ramírez",
            specialty = "Dermatología",
            date = "Mié 26",
            time = "11:30 a. m.",
            estado = EstadoCita.CONFIRMADA,
        ),
        Cita(
            id = 3,
            doctorId = 3,
            doctorName = "Dra. Lucía Mendoza",
            specialty = "Pediatría",
            date = "Mar 18",
            time = "3:00 p. m.",
            estado = EstadoCita.COMPLETADA,
        ),
        Cita(
            id = 4,
            doctorId = 4,
            doctorName = "Dr. Andrés Paredes",
            specialty = "Traumatología",
            date = "Jue 13",
            time = "10:15 a. m.",
            estado = EstadoCita.COMPLETADA,
        ),
        Cita(
            id = 5,
            doctorId = 1,
            doctorName = "Dra. María Fernández",
            specialty = "Cardiología",
            date = "Vie 21",
            time = "4:45 p. m.",
            estado = EstadoCita.COMPLETADA,
        ),
    )
}