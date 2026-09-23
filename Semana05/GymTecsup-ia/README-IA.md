# TECSUP Fit

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para la reserva y gestión de clases de gimnasio.

## Requerimientos funcionales

### RF01 - Visualización y filtrado de clases
La aplicación debe mostrar las clases disponibles en la pantalla Inicio mediante un LazyColumn.

Cada clase debe mostrar:
- Nombre de la clase.
- Horario.

La pantalla también debe incluir un LazyRow con los filtros:
- Hoy.
- Esta semana.

---

### RF02 - Detalle de clase
La aplicación debe permitir seleccionar una clase y acceder a su pantalla de detalle.

El detalle debe recibir los datos de la clase seleccionada mediante navegación y mostrar:
- Nombre de la clase.
- Horario.
- Botón "Reservar cupo".

---

### RF03 - Confirmación de reserva
Después de reservar una clase, la aplicación debe mostrar una pantalla de confirmación.

Debe mostrar:
- Nombre de la clase.
- Horario de la clase.
- Información de la reserva.

---

### RF04 - Navegación secundaria
La aplicación debe implementar una navegación secundaria mediante BottomBar.

Debe contener 4 destinos:
- Inicio.
- Reservas.
- Rutinas.
- Perfil.

El ícono de la sección activa debe estar visualmente diferenciado.

---

### RF05 - Gestión de reservas y perfil
La pantalla Reservas debe mostrar las clases reservadas mediante un LazyColumn.

Las reservas deben diferenciar visualmente los estados:
- Confirmada.
- Completada.

La pantalla Perfil debe mostrar:
- Datos del usuario.
- Clases tomadas.
- Racha de asistencia.