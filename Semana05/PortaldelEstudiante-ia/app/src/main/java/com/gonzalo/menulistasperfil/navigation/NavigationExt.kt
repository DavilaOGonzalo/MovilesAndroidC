package com.gonzalo.menulistasperfil.navigation

import androidx.navigation.NavController

// Cierre de sesión: regresa al Login limpiando el back stack de las pantallas
// autenticadas. Al quedar Login como raíz, el botón atrás no devuelve al usuario
// a Home, List o Profile.
fun NavController.logout() {
    navigate(Screen.Login.route) {
        popUpTo(Screen.Login.route) { inclusive = false }
        launchSingleTop = true
    }
}