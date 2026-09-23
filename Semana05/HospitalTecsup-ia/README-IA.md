# Clínica Salud+

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para la gestión y reserva de citas médicas.

## Requerimientos funcionales

### RF01 - Visualización de médicos

La pantalla de inicio debe mostrar un `LazyRow` con chips de especialidad y un `LazyColumn` con una lista de médicos.

Cada tarjeta de médico debe mostrar:
- Nombre.
- Especialidad.
- Calificación.

### RF02 - Perfil del médico

La aplicación debe permitir seleccionar un médico desde la lista y navegar a su perfil mediante el paso de parámetros de navegación.

El perfil debe mostrar la información del médico seleccionado e incluir el botón **"Agendar cita"**.

### RF03 - Agendamiento de cita

La aplicación debe permitir seleccionar una fecha y una hora para la cita.

Debe mostrar como mínimo:
- 3 opciones de fecha.
- 3 opciones de hora.

La selección de fecha y hora debe ser única.

### RF04 - Confirmación de la cita

Después de confirmar la cita, la aplicación debe mostrar un resumen con:
- Médico.
- Fecha.
- Hora.

La pantalla de confirmación debe incluir un botón para volver al inicio.

### RF05 - Navegación secundaria y mis citas

La aplicación debe contar con un menú lateral (`NavigationDrawer`) accesible desde la pantalla de inicio mediante el ícono ☰.

El menú debe incluir como mínimo:
- Inicio.
- Mis citas.
- Historial médico.

La sección **Mis citas** debe utilizar un `LazyColumn` para mostrar las citas agendadas y diferenciar visualmente sus estados:
- Confirmada.
- Completada.