package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    val usuarios = mutableListOf<Usuario>()
    var usuarioActual: Usuario? = null
    private var siguienteId = 1

    val especialidades = mutableListOf(
        Especialidad(1, "Cardiología", "Enfermedades del corazón y sistema circulatorio", destacada = true),
        Especialidad(2, "Dermatología", "Cuidado de la piel, cabello y uñas", destacada = true),
        Especialidad(3, "Pediatría", "Atención médica de niños y adolescentes", destacada = true),
        Especialidad(4, "Traumatología", "Huesos, músculos y articulaciones"),
        Especialidad(5, "Neurología", "Sistema nervioso y trastornos cerebrales"),
        Especialidad(6, "Oftalmología", "Salud visual y enfermedades de los ojos")
    )

    val medicos = mutableListOf(
        Medico(1, "Ana Torres", 1, 4.8, 12),
        Medico(2, "Luis Mendoza", 1, 4.5, 8),
        Medico(3, "Carla Ríos", 2, 4.9, 10),
        Medico(4, "Pedro Gómez", 3, 4.7, 15),
        Medico(5, "María López", 3, 4.6, 9),
        Medico(6, "Jorge Vega", 4, 4.3, 7),
        Medico(7, "Lucía Nava", 5, 4.8, 11),
        Medico(8, "Diego Cruz", 6, 4.4, 6)
    )

    val citas = mutableListOf<Cita>()
    private var siguienteIdCita = 1

    val fechasDisponibles = listOf(
        "Lun 13/10", "Mar 14/10", "Mié 15/10", "Jue 16/10", "Vie 17/10"
    )

    val horarios = listOf(
        "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
    )

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val reservados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horarios.filter { it !in reservados }
    }

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

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        return especialidades.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
                    it.descripcion.contains(texto, ignoreCase = true)
        }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.filter { it.destacada }
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(texto: String): List<Medico> {
        return medicos
            .filter { medico ->
                medico.nombre.contains(texto, ignoreCase = true) ||
                        (obtenerEspecialidad(medico.especialidadId)?.nombre
                            ?.contains(texto, ignoreCase = true) == true)
            }
            .sortedByDescending { it.calificacion }
    }
}
