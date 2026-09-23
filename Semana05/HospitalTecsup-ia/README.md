# Clínica Salud+

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para gestionar la reserva de citas médicas.

## Requerimientos funcionales

### RF01 - Visualización de médicos

La aplicación debe mostrar en la pantalla de inicio una lista de médicos disponibles mediante un `LazyColumn`.

Cada médico debe mostrar como mínimo:

- Nombre.
- Especialidad.
- Calificación.

La pantalla también debe permitir filtrar médicos mediante un `LazyRow` con chips de especialidad.

### RF02 - Visualización del perfil del médico

La aplicación debe permitir seleccionar un médico desde la lista y navegar hacia su perfil.

El perfil debe recibir mediante navegación los datos del médico seleccionado y mostrar su información correspondiente.

La pantalla debe incluir un botón **"Agendar cita"** para continuar con el proceso de reserva.

### RF03 - Agendamiento de una cita médica

La aplicación debe permitir seleccionar una fecha y una hora para la cita.

La fecha debe ofrecer como mínimo 3 opciones y la hora como mínimo 3 opciones, permitiendo seleccionar únicamente una opción de cada conjunto.

Después de seleccionar la fecha y hora, la aplicación debe permitir confirmar la cita.