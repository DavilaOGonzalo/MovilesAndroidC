package com.gonzalo.gymtecsup_mio.data

data class Reserva(
    val id: Int,
    val className: String,
    val schedule: String,
    val instructor: String,
)

object ReservaCatalog {
    private val _reservas = mutableListOf<Reserva>()

    val reservas: List<Reserva> get() = _reservas.toList()

    fun add(fitnessClass: FitnessClass) {
        if (_reservas.none { it.id == fitnessClass.id }) {
            _reservas.add(
                Reserva(
                    id = fitnessClass.id,
                    className = fitnessClass.name,
                    schedule = fitnessClass.schedule,
                    instructor = fitnessClass.instructor,
                ),
            )
        }
    }
}