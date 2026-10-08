package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

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
        Medico(1, "Ana Torres", 1, 4.8, 12, fotoDoctor(15752232)),
        Medico(2, "Luis Mendoza", 1, 4.5, 8, fotoDoctor(5722163)),
        Medico(3, "Carla Ríos", 2, 4.9, 10, fotoDoctor(19963166)),
        Medico(4, "Pedro Gómez", 3, 4.7, 15, fotoDoctor(6762869)),
        Medico(5, "María López", 3, 4.6, 9, fotoDoctor(5452195)),
        Medico(6, "Jorge Vega", 4, 4.3, 7, fotoDoctor(12660379)),
        Medico(7, "Lucía Nava", 5, 4.8, 11, fotoDoctor(8376309)),
        Medico(8, "Diego Cruz", 6, 4.4, 6, fotoDoctor(6762862))
    )

    private fun fotoDoctor(id: Int): String {
        return "https://images.pexels.com/photos/$id/pexels-photo-$id.jpeg" +
                "?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop"
    }

    val citas = mutableListOf<Cita>()
    private var siguienteIdCita = 1

    val horarios = listOf(
        "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
    )

    const val clinicaNombre = "Clínica SaludPlus"
    const val clinicaDireccion = "Av. Central 123, Lima"

    fun semanaActual(): LocalDate {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    fun diasHabilesDeLaSemana(inicioSemana: LocalDate): List<LocalDate> {
        return (0..6)
            .map { inicioSemana.plusDays(it.toLong()) }
            .filter {
                it.dayOfWeek != DayOfWeek.SATURDAY &&
                        it.dayOfWeek != DayOfWeek.SUNDAY &&
                        !it.isBefore(LocalDate.now())
            }
    }

    fun formatearFecha(fecha: LocalDate): String {
        val dia = when (fecha.dayOfWeek) {
            DayOfWeek.MONDAY -> "Lun"
            DayOfWeek.TUESDAY -> "Mar"
            DayOfWeek.WEDNESDAY -> "Mié"
            DayOfWeek.THURSDAY -> "Jue"
            DayOfWeek.FRIDAY -> "Vie"
            else -> ""
        }
        return "$dia ${fecha.dayOfMonth}/${fecha.monthValue}"
    }

    private fun mesEnEspanol(mes: Int): String = when (mes) {
        1 -> "enero"
        2 -> "febrero"
        3 -> "marzo"
        4 -> "abril"
        5 -> "mayo"
        6 -> "junio"
        7 -> "julio"
        8 -> "agosto"
        9 -> "setiembre"
        10 -> "octubre"
        11 -> "noviembre"
        else -> "diciembre"
    }

    fun nombreMesYAnio(fecha: LocalDate): String {
        return mesEnEspanol(fecha.monthValue).replaceFirstChar { it.uppercase() } +
                " de ${fecha.year}"
    }

    fun fechaLarga(fecha: LocalDate): String {
        val dia = when (fecha.dayOfWeek) {
            DayOfWeek.MONDAY -> "Lunes"
            DayOfWeek.TUESDAY -> "Martes"
            DayOfWeek.WEDNESDAY -> "Miércoles"
            DayOfWeek.THURSDAY -> "Jueves"
            DayOfWeek.FRIDAY -> "Viernes"
            DayOfWeek.SATURDAY -> "Sábado"
            DayOfWeek.SUNDAY -> "Domingo"
        }
        return "$dia ${fecha.dayOfMonth} de ${mesEnEspanol(fecha.monthValue)} ${fecha.year}"
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val reservados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horarios.filter { it !in reservados }
    }

    fun proximaDisponibilidad(medicoId: Int): String {
        val hoy = LocalDate.now()
        for (i in 0..10) {
            val fecha = hoy.plusDays(i.toLong())
            if (fecha.dayOfWeek == DayOfWeek.SATURDAY ||
                fecha.dayOfWeek == DayOfWeek.SUNDAY
            ) {
                continue
            }
            if (horariosDisponibles(medicoId, fecha.toString()).isNotEmpty()) {
                return when (i) {
                    0 -> "Disponible hoy"
                    1 -> "Disponible mañana"
                    else -> "Disponible ${formatearFecha(fecha)}"
                }
            }
        }
        return "Sin disponibilidad"
    }

    fun citasDelUsuario(): List<Cita> {
        val usuario = usuarioActual ?: return emptyList()
        return citas.filter { it.usuarioId == usuario.id }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        val medico = obtenerMedico(medicoId) ?: return false
        val usuario = usuarioActual ?: return false
        val yaReservada = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (yaReservada) return false
        val cita = Cita(
            id = siguienteIdCita,
            usuarioId = usuario.id,
            medicoId = medicoId,
            especialidadId = medico.especialidadId,
            fecha = fecha,
            hora = hora
        )
        siguienteIdCita++
        citas.add(cita)
        return true
    }

    fun registrarUsuario(nombre: String, email: String, password: String): Boolean {
        if (usuarios.any { it.email == email }) return false
        val usuario = Usuario(
            id = siguienteId,
            nombre = nombre,
            email = email,
            password = password,
            fotoUrl = fotoDeUsuario()
        )
        siguienteId++
        usuarios.add(usuario)
        return true
    }

    fun iniciarSesion(email: String, password: String): Boolean {
        val usuario = usuarios.find { it.email == email && it.password == password } ?: return false
        if (usuario.fotoUrl.isBlank()) {
            usuarioActual = usuario.copy(fotoUrl = fotoDeUsuario()).also {
                usuarios[usuarios.indexOfFirst { u -> u.id == usuario.id }] = it
            }
        } else {
            usuarioActual = usuario
        }
        return true
    }

    private fun fotoDeUsuario(): String {
        return "https://images.pexels.com/photos/19438563/pexels-photo-19438563.jpeg" +
                "?auto=compress&cs=tinysrgb&w=400&h=400&fit=crop"
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
