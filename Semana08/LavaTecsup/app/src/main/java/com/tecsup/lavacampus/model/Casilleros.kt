package com.tecsup.lavacampus.model

object Casilleros {
    val todos: List<String> = (1..30).map { "C-${it.toString().padStart(2, '0')}" }
}
