package com.gonzalo.menulistasperfil.data

import androidx.compose.ui.graphics.Color
import com.gonzalo.menulistasperfil.ui.theme.AvatarIndigo
import com.gonzalo.menulistasperfil.ui.theme.AvatarLilac
import com.gonzalo.menulistasperfil.ui.theme.AvatarOrchid
import com.gonzalo.menulistasperfil.ui.theme.AvatarPeriwinkle
import com.gonzalo.menulistasperfil.ui.theme.AvatarPlum
import com.gonzalo.menulistasperfil.ui.theme.AvatarViolet

// Datos estáticos del directorio de alumnos (sin base de datos ni API).
data class Student(
    val id: Int,
    val studentCode: String,
    val name: String,
    val career: String,
    val email: String,
    val faculty: String,
    val biography: String,
    val avatarColor: Color,
)

// Datos del usuario que inició sesión.
data class User(
    val fullName: String,
    val email: String,
    val phone: String,
    val career: String,
    val cycle: String,
    val faculty: String,
    val studentCode: String,
)

object UserSession {
    // Credenciales de demostración estáticas (no hay autenticación real).
    const val DEMO_PASSWORD = "tecsup2026"

    val currentUser = User(
        fullName = "Juan León",
        email = "juan.leon@tecsup.edu.pe",
        phone = "+51 987 654 321",
        career = "Ingeniería de Sistemas e Informática",
        cycle = "6to ciclo",
        faculty = "Facultad de Ingeniería y Computación",
        studentCode = "2021-10428",
    )
}

object StudentCatalog {
    val students = listOf(
        Student(
            id = 1,
            studentCode = "2021-10012",
            name = "María Fernanda Quispe",
            career = "Ciencias de la Computación",
            email = "maria.quispe@tecsup.edu.pe",
            faculty = "Facultad de Ciencias de la Computación",
            biography = "Estudiante de octavo ciclo con interés en inteligencia artificial y " +
                    "desarrollo de aplicaciones móviles. Participa activamente en el club de " +
                    "robótica y en hackatones universitarios.",
            avatarColor = AvatarViolet,
        ),
        Student(
            id = 2,
            studentCode = "2022-11345",
            name = "Carlos Ramírez Torres",
            career = "Ingeniería de Software",
            email = "carlos.ramirez@tecsup.edu.pe",
            faculty = "Facultad de Ingeniería",
            biography = "Apasionado por el desarrollo backend y las arquitecturas limpias. " +
                    "Líder del equipo de desarrollo del taller de programación y delegado del " +
                    "consejo estudiantil.",
            avatarColor = AvatarIndigo,
        ),
        Student(
            id = 3,
            studentCode = "2021-10782",
            name = "Lucía Mendoza Rojas",
            career = "Diseño Gráfico Digital",
            email = "lucia.mendoza@tecsup.edu.pe",
            faculty = "Facultad de Diseño",
            biography = "Creativa y orientada a la experiencia de usuario. Ha participado en " +
                    "concursos de ilustración digital y en el diseño de identidad de marcas " +
                    "universitarias.",
            avatarColor = AvatarLilac,
        ),
        Student(
            id = 4,
            studentCode = "2022-11560",
            name = "Diego Torres Salazar",
            career = "Ingeniería de Sistemas e Informática",
            email = "diego.torres@tecsup.edu.pe",
            faculty = "Facultad de Ingeniería y Computación",
            biography = "Interesado en ciberseguridad y redes. Miembro del grupo de " +
                    "investigación en seguridad informática de la facultad y voluntario del " +
                    "equipo de soporte técnico.",
            avatarColor = AvatarPlum,
        ),
        Student(
            id = 5,
            studentCode = "2020-09871",
            name = "Sofía Gutiérrez Paredes",
            career = "Ingeniería de Software",
            email = "sofia.gutierrez@tecsup.edu.pe",
            faculty = "Facultad de Ingeniería",
            biography = "Desarrolladora móvil en formación. Disfruta crear interfaces limpias " +
                    "y aprender nuevas tecnologías del ecosistema Android. Finalista del " +
                    "concurso de apps de la universidad.",
            avatarColor = AvatarPeriwinkle,
        ),
        Student(
            id = 6,
            studentCode = "2022-11204",
            name = "Andrés Paredes Vela",
            career = "Redes y Comunicaciones",
            email = "andres.paredes@tecsup.edu.pe",
            faculty = "Facultad de Tecnología",
            biography = "Futuro especialista en infraestructura de red. Colabora en el soporte " +
                    "técnico del laboratorio de telecomunicaciones y practica con simuladores " +
                    "de equipos de red.",
            avatarColor = AvatarOrchid,
        ),
    )

    fun findById(id: Int): Student =
        students.firstOrNull { it.id == id } ?: students.first()
}