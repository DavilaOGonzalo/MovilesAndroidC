package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val especialidadId: Int,
    val fecha: String,
    val hora: String
)
