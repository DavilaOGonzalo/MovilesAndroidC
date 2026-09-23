# TECSUP Fit

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para la reserva de clases de gimnasio.

## Requerimientos funcionales

### RF01 - Visualización y filtrado de clases

La pantalla de inicio debe mostrar un `LazyRow` con chips de filtro y un `LazyColumn` con una lista de clases disponibles.

Los filtros deben incluir como mínimo:

- Hoy.
- Esta semana.

La lista debe mostrar como mínimo 3 clases, indicando el nombre y horario de cada una.

### RF02 - Detalle y reserva de clase

La aplicación debe permitir seleccionar una clase desde la lista y navegar hacia su detalle mediante parámetros de navegación.

La pantalla de detalle debe mostrar la información de la clase seleccionada e incluir el botón **"Reservar cupo"**.

### RF03 - Confirmación de reserva

Después de reservar una clase, la aplicación debe mostrar una pantalla de confirmación con un resumen de:

- Clase.
- Horario.

La pantalla debe incluir un botón para acceder a las reservas.