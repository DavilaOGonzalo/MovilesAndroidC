package com.gonzalo.hospitaltecsup_ia.data

// Modelo de datos estático de un médico de Clínica Salud+.
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val description: String,
)

// Catálogo estático de médicos (sin base de datos ni API).
object DoctorCatalog {
    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. Ana Torres",
            specialty = "Cardiología",
            rating = 4.9,
            description = "Especialista en el diagnóstico y tratamiento de las enfermedades del corazón y del sistema circulatorio.",
        ),
        Doctor(
            id = 2,
            name = "Dr. Luis Vega",
            specialty = "Pediatría",
            rating = 4.7,
            description = "Atención integral de la salud de niños y adolescentes, desde el nacimiento hasta la adolescencia.",
        ),
        Doctor(
            id = 3,
            name = "Dra. Rosa Díaz",
            specialty = "Dermatología",
            rating = 4.8,
            description = "Prevención y tratamiento de las enfermedades de la piel, el cabello y las uñas.",
        ),
    )

    fun findById(id: Int): Doctor? =
        doctors.firstOrNull { it.id == id }
}