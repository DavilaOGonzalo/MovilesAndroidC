package com.gonzalo.gymtecsup_mio.navigation

object Screen {
    const val HOME = "home"
    const val CLASS_DETAIL = "class_detail/{classId}"

    fun classDetail(classId: Int): String = "class_detail/$classId"
}