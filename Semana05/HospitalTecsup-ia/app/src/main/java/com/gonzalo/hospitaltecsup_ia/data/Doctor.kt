package com.gonzalo.hospitaltecsup_ia.data

// Modelo de datos estático de un médico de Clínica Salud+.
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
)

// Catálogo estático de médicos (sin base de datos ni API).
object DoctorCatalog {
    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. María Fernández",
            specialty = "Cardiología",
            rating = 4.8,
        ),
        Doctor(
            id = 2,
            name = "Dr. Carlos Ramírez",
            specialty = "Dermatología",
            rating = 4.6,
        ),
        Doctor(
            id = 3,
            name = "Dra. Lucía Mendoza",
            specialty = "Pediatría",
            rating = 4.9,
        ),
        Doctor(
            id = 4,
            name = "Dr. Andrés Paredes",
            specialty = "Traumatología",
            rating = 4.5,
        ),
    )

    fun findById(id: Int): Doctor? =
        doctors.firstOrNull { it.id == id }
}