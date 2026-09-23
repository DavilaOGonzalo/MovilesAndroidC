package com.gonzalo.menulistasperfil.navigation

// Contrato central de navegación. Cada pantalla es un objeto con su ruta única.
sealed class Screen(val route: String) {

    // Pantalla de acceso — punto de entrada de la app
    object Login : Screen("login")

    // Pantalla de recuperación de contraseña
    object RecoverPassword : Screen("recover_password")

    // Pantalla de inicio después del login
    object Home : Screen("home")

    // Pantalla que muestra el directorio de alumnos
    object List : Screen("list")

    // Pantalla del perfil del usuario
    object Profile : Screen("profile")

    // RUTA CON ARGUMENTO
    // {itemId} es el placeholder que Navigation reemplaza
    // con el valor real al momento de navegar.
    object Detail : Screen("detail/{itemId}") {

        // Construye la ruta final sustituyendo el placeholder por el valor real.
        // Ejemplo: createRoute(5) → devuelve "detail/5"
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}