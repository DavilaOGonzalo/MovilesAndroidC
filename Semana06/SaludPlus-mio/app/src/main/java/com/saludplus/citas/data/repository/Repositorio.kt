package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Usuario

object Repositorio {

    val usuarios = mutableListOf<Usuario>()
    var usuarioActual: Usuario? = null
    private var siguienteId = 1

    fun registrarUsuario(nombre: String, email: String, password: String): Boolean {
        if (usuarios.any { it.email == email }) return false
        val usuario = Usuario(id = siguienteId, nombre = nombre, email = email, password = password)
        siguienteId++
        usuarios.add(usuario)
        return true
    }

    fun iniciarSesion(email: String, password: String): Boolean {
        val usuario = usuarios.find { it.email == email && it.password == password } ?: return false
        usuarioActual = usuario
        return true
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
