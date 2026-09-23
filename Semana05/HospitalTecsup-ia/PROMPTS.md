# Clínica Salud+

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para la gestión y reserva de citas médicas.

## Tecnologías

- Kotlin
- Jetpack Compose
- Navigation Compose
- LazyColumn
- LazyRow
- Scaffold
- NavigationDrawer
- remember / mutableStateOf

---

# Fase 1 — Desarrollo sin IA

## Parte 1 — Estructura y datos de médicos

Se preparó la estructura inicial de la aplicación y se crearon los datos estáticos de los médicos.

Se definieron como mínimo 3 médicos con:

- Nombre.
- Especialidad.
- Calificación.

---

## Parte 2 — Pantalla de inicio

Se implementó la pantalla principal de Clínica Salud+.

La pantalla contiene el título de la aplicación y la estructura necesaria para mostrar las especialidades y los médicos disponibles.

---

## Parte 3 — Especialidades

Se implementó un `LazyRow` para mostrar las especialidades médicas mediante chips.

Se incluyeron como mínimo:

- Cardiología.
- Pediatría.

Los chips permiten seleccionar una especialidad.

---

## Parte 4 — Lista de médicos

Se implementó un `LazyColumn` para mostrar la lista de médicos.

Cada tarjeta contiene:

- Nombre.
- Especialidad.
- Calificación.

Se incluyeron como mínimo 3 médicos.

---

## Parte 5 — Perfil del médico y navegación

Se implementó la navegación desde la lista de médicos hacia el perfil del médico seleccionado.

El perfil recibe los datos del médico mediante parámetros de navegación.

También se agregó el botón:

**"Agendar cita"**

---

## Parte 6 — Agendar cita

Se implementó la pantalla para realizar el agendamiento de una cita.

La pantalla permite seleccionar:

- Fecha.
- Hora.

Se incluyeron como mínimo 3 opciones de fecha y 3 opciones de hora.

---

## Parte 7 — Selección única

Se implementó la selección única de fecha y hora.

El usuario solamente puede seleccionar:

- Una fecha.
- Una hora.

La opción seleccionada se diferencia visualmente.

También se agregó el botón:

**"Confirmar cita"**

---

## Parte 8 — Confirmación

Se implementó la pantalla de confirmación de la cita.

La pantalla muestra un resumen con:

- Médico.
- Fecha.
- Hora.

También incluye un botón para volver al inicio.

---

# Fase 2 — Mejora con IA

## Parte 9 — NavigationDrawer

Se implementó un menú lateral mediante `NavigationDrawer`.

El menú se puede abrir desde el ícono ☰ de la pantalla de inicio.

Incluye los siguientes destinos:

- Inicio.
- Mis citas.
- Historial médico.

Se mantuvo la navegación mediante componentes independientes.

---

## Parte 10 — Mis citas

Se implementó la pantalla **Mis citas**.

La pantalla utiliza `LazyColumn` para mostrar las citas agendadas.

Cada cita muestra:

- Médico.
- Fecha.
- Hora.
- Estado.

Los estados utilizados son:

- Confirmada.
- Completada.

Los estados se diferencian visualmente.

---

## Parte 11 — Diseño visual

Se mejoró la interfaz de la aplicación tomando como referencia el diseño proporcionado para Clínica Salud+.

Se aplicaron:

- Tonos morados como color principal.
- Fondos claros.
- Tarjetas con bordes redondeados.
- Botones morados.
- Espaciado uniforme.
- Iconos.
- Diseño limpio y minimalista.

### Inicio

La pantalla incluye:

- Encabezado morado.
- Nombre "Clínica Salud+".
- Chips de especialidades.
- Lista de médicos.
- Tarjetas con nombre, especialidad y calificación.

### Perfil del médico

Incluye:

- Botón para regresar.
- Información del médico.
- Calificación.
- Descripción.
- Botón "Agendar cita".

### Agendar cita

Incluye:

- Selección de fecha.
- Selección de hora.
- Selección única.
- Botón "Confirmar cita".

### Confirmación

Incluye:

- Indicador de éxito.
- Mensaje "¡Cita agendada!".
- Médico.
- Fecha.
- Hora.
- Botón para volver al inicio.

### Mis citas

Incluye:

- Lista de citas mediante `LazyColumn`.
- Información de cada cita.
- Estados "Confirmada" y "Completada".
- Diferenciación visual de los estados.

### NavigationDrawer

Incluye:

- Inicio.
- Mis citas.
- Historial médico.

---

# Requerimientos funcionales

## RF01 — Visualización de médicos

La aplicación debe mostrar un `LazyRow` con chips de especialidad y un `LazyColumn` con una lista de médicos.

Cada tarjeta debe mostrar:

- Nombre.
- Especialidad.
- Calificación.

## RF02 — Perfil del médico

La aplicación debe permitir seleccionar un médico y navegar a su perfil mediante parámetros de navegación.

El perfil debe mostrar la información del médico seleccionado e incluir el botón **"Agendar cita"**.

## RF03 — Agendamiento de cita

La aplicación debe permitir seleccionar una fecha y una hora.

Debe contar con:

- Mínimo 3 fechas.
- Mínimo 3 horas.
- Selección única de fecha.
- Selección única de hora.

## RF04 — Confirmación

La aplicación debe mostrar un resumen de la cita agendada:

- Médico.
- Fecha.
- Hora.

Debe incluir un botón para volver al inicio.

## RF05 — Navegación secundaria y mis citas

La aplicación debe contar con un `NavigationDrawer` con mínimo 3 destinos:

- Inicio.
- Mis citas.
- Historial médico.

La sección Mis citas debe utilizar un `LazyColumn` y mostrar las citas con los estados:

- Confirmada.
- Completada.

---

# Ramas

## Main

Contiene el desarrollo de la Fase 1 realizado sin IA.

- 8 commits mínimos.

## Mejora-ia

Contiene las mejoras realizadas con IA.

- 3 commits mínimos.
- NavigationDrawer.
- Mis citas.
- Mejora visual.

---

# Commits

### Fase 1

1. `feat: crea estructura y datos de médicos`
2. `feat: implementa pantalla de inicio`
3. `feat: agrega filtros de especialidad`
4. `feat: implementa lista de médicos`
5. `feat: agrega perfil y navegación de médicos`
6. `feat: implementa pantalla de agendamiento`
7. `feat: implementa seleccion unica de fecha y hora`
8. `feat: implementa confirmacion de cita`

### Fase 2

9. `feat: implementa navigation drawer`
10. `feat: implementa pantalla de mis citas`
11. `style: mejora diseño visual de la aplicacion`