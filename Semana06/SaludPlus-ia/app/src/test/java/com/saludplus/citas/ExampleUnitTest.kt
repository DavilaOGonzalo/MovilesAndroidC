package com.saludplus.citas

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun horariosReservadosNoAparecenDisponibles() {
        Repositorio.citas.clear()
        val fecha = LocalDate.now().toString()
        val horaReservada = Repositorio.horarios.first()
        Repositorio.citas.add(
            Cita(
                id = 999,
                usuarioId = 1,
                medicoId = 1,
                especialidadId = 1,
                fecha = fecha,
                hora = horaReservada
            )
        )

        val disponibles = Repositorio.horariosDisponibles(1, fecha)

        assertFalse(horaReservada in disponibles)
        assertEquals(Repositorio.horarios.size - 1, disponibles.size)

        val otroMedico = Repositorio.horariosDisponibles(2, fecha)
        assertEquals(Repositorio.horarios.size, otroMedico.size)

        Repositorio.citas.clear()
    }

    @Test
    fun diasHabilesExcluyenFinDeSemanaYPasados() {
        val semana = Repositorio.semanaActual()
        val dias = Repositorio.diasHabilesDeLaSemana(semana)

        assertTrue(dias.all { it.dayOfWeek != DayOfWeek.SATURDAY })
        assertTrue(dias.all { it.dayOfWeek != DayOfWeek.SUNDAY })
        assertTrue(dias.all { !it.isBefore(LocalDate.now()) })
        assertTrue(dias.all { !it.isAfter(semana.plusDays(4)) })
    }

    @Test
    fun fechaLargaMuestraDiaYMesEnEspanol() {
        assertEquals(
            "Martes 13 de octubre 2026",
            Repositorio.fechaLarga(LocalDate.of(2026, 10, 13))
        )
        assertEquals(
            "Miércoles 16 de setiembre 2026",
            Repositorio.fechaLarga(LocalDate.of(2026, 9, 16))
        )
    }
}
