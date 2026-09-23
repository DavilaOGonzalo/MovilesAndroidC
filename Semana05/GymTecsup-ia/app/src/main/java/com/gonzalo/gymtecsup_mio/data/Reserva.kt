package com.gonzalo.gymtecsup_mio.data

enum class EstadoReserva {
    CONFIRMADA,
    COMPLETADA,
}

data class Reserva(
    val id: Int,
    val className: String,
    val schedule: String,
    val instructor: String,
    val estado: EstadoReserva,
)

object ReservaCatalog {
    private val _reservas = mutableListOf(
        Reserva(
            id = 101,
            className = "Yoga & Estiramiento",
            schedule = "Lun · Mié · 7:00 a.m.",
            instructor = "Prof. Karen Rivera",
            estado = EstadoReserva.CONFIRMADA,
        ),
        Reserva(
            id = 102,
            className = "Spinning",
            schedule = "Lun · Vie · 8:00 p.m.",
            instructor = "Coach Laura Méndez",
            estado = EstadoReserva.COMPLETADA,
        ),
        Reserva(
            id = 103,
            className = "Zumba",
            schedule = "Mié · Vie · 6:00 p.m.",
            instructor = "Prof. Sofía Quispe",
            estado = EstadoReserva.COMPLETADA,
        ),
    )

    val reservas: List<Reserva> get() = _reservas.toList()

    fun add(fitnessClass: FitnessClass) {
        if (_reservas.none { it.id == fitnessClass.id }) {
            _reservas.add(
                Reserva(
                    id = fitnessClass.id,
                    className = fitnessClass.name,
                    schedule = fitnessClass.schedule,
                    instructor = fitnessClass.instructor,
                    estado = EstadoReserva.CONFIRMADA,
                ),
            )
        }
    }
}