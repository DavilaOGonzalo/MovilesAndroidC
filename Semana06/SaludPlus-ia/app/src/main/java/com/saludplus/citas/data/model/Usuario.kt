package com.saludplus.citas.data.model

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val fotoUrl: String = ""
)
