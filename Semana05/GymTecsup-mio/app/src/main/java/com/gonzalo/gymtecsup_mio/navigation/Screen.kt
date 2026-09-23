package com.gonzalo.gymtecsup_mio.navigation

object Screen {
    const val HOME = "home"
    const val CLASS_DETAIL = "class_detail/{classId}"
    const val CONFIRMATION = "confirmation/{classId}"
    const val RESERVAS = "reservas"

    fun classDetail(classId: Int): String = "class_detail/$classId"

    fun confirmation(classId: Int): String = "confirmation/$classId"
}