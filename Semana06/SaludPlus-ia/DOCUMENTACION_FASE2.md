# Documentación final de la Fase 2

## VII. Preguntas de reflexión

### 1. ¿Por qué los modelos, `Rutas.kt` y `AppNavigation.kt` se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?

Los modelos, `Rutas.kt` y `AppNavigation.kt` forman la base fija de la aplicación. Los modelos definen cómo se representan los usuarios, médicos, especialidades y citas. `Rutas.kt` define los nombres y parámetros de las rutas, mientras que `AppNavigation.kt` conecta esas rutas con cada pantalla. Si esa parte estuviera incompleta, todas las pantallas podrían manejar datos o navegación de forma diferente.

Las pantallas se dejaron como esqueleto porque eran la parte que debía completar el estudiante: diseño, componentes de Compose, estados, validaciones y acciones de cada botón. Los archivos que se dejaron como esqueleto tienen en común que pertenecen principalmente a la capa visual (`ui/screens` y componentes). Dependen de los modelos y de la navegación, pero permiten distintas soluciones de distribución y diseño.

### 2. ¿Por qué el Repositorio es un `object` y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista?

`Repositorio` es un `object` para tener una única instancia compartida en toda la aplicación. Así, las pantallas consultan y modifican las mismas listas de usuarios, médicos y citas.

Si cada pantalla creara su propia lista, una cita registrada en ConfirmarCitaScreen no aparecería necesariamente en MisCitasScreen. También podrían repetirse horarios, no bloquearse las horas reservadas y perderse los datos al cambiar de pantalla. El `object` evita esas copias y mantiene una fuente de datos común.

### 3. ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos, sin que tú "actualices" nada a mano?

Se usó el estado de Compose con `remember` y `mutableStateOf`. En EspecialidadesScreen, cada cambio del texto modifica `texto` y se vuelve a ejecutar `Repositorio.buscarEspecialidades(texto)`. Compose recompone la parte de la pantalla que depende de ese valor.

En FechaHoraScreen, `fechaSeleccionada` es un estado. La lista `horariosDisponibles` se obtiene nuevamente con `Repositorio.horariosDisponibles(medicoId, fechaSeleccionada.toString())` cada vez que cambia la fecha. Además, al cambiar de día se coloca `horaSeleccionada` en `null`. Por eso la lista y la selección se actualizan mediante recomposición, sin llamar manualmente a un método de refresco.

### 4. ¿Qué diferencia notaste entre `navigate()` normal y el que usa `popUpTo`? ¿Qué pasa al presionar Atrás en cada caso?

En la navegación normal, por ejemplo de Especialidades a Médicos, `navigate()` agrega la nueva pantalla a la pila. Al presionar Atrás se regresa a Especialidades y se conserva la navegación anterior.

Después de confirmar una cita se usa `navigate()` con `popUpTo(Rutas.ESPECIALIDADES) { inclusive = true }`. Esto elimina de la pila Especialidades y las pantallas del proceso de agendamiento. Al llegar a CitaExitosa, Atrás ya no recorre nuevamente Médico, FechaHora y Confirmación; queda la pantalla principal anterior. Esto evita que el usuario vuelva a un formulario de cita que ya fue confirmado.

### 5. ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?

Se corrigieron varios puntos:

- Se reemplazaron fechas fijas por fechas calculadas con `LocalDate`.
- Se filtraron sábados, domingos y días anteriores a la fecha actual.
- Se agregó el límite para no retroceder antes de la semana actual.
- Al cambiar de día se recalculan los horarios y se reinicia la hora seleccionada.
- Se cambió la fecha que viaja internamente por la clave ISO de `LocalDate`, por ejemplo `2026-10-13`, para que `horariosDisponibles` y `agendarCita` usen exactamente el mismo valor.
- Se agregó el formato largo en español para ConfirmarCitaScreen.
- Se evitó usar `Locale.of`, porque no es compatible con todos los dispositivos indicados por el `minSdk`; se creó un helper manual para los meses en español.
- Se actualizaron las pruebas para comprobar horarios reservados, días hábiles y fechas en español.

### 6. Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?

Usaría un `NavigationDrawer` cuando la aplicación tenga muchas secciones, opciones de configuración o acciones secundarias. Es útil porque el menú aparece desde un lateral y no ocupa espacio permanente en la pantalla.

Usaría un `NavigationBar` cuando existan pocas secciones principales que el usuario necesite consultar con frecuencia desde un celular. En esta tarea se usa para Inicio, Citas, Reportes y Perfil, por lo que permite cambiar rápidamente entre las áreas principales y queda visible en la parte inferior.

## VIII. Observaciones y conclusiones

### Observaciones

- Uno de los problemas encontrados fue que una versión generada para los meses en español usaba `Locale.of`, que requiere una versión de Android más nueva que la soportada por el proyecto. Se reemplazó por una función manual para evitar un error en dispositivos con `minSdk 26`.
- Al principio la fecha visible y la fecha usada para reservar no tenían el mismo formato. La pantalla mostraba una etiqueta corta, pero el repositorio necesitaba una clave consistente. Se corrigió usando `LocalDate` en formato ISO para la lógica y dejando el formato corto solamente para mostrarlo.
- La fila original de días podía desbordarse en pantallas pequeñas. Fue necesario revisar el tamaño de las tarjetas y la distribución para que el calendario se adaptara mejor.
- La referencia visual tenía un paso de agendamiento de "Motivo y lugar" que no existía como pantalla separada. Se agregó `MotivoLugarScreen` y su ruta, sin quitar la confirmación ni la función que registra la cita.
- Algunos iconos de la referencia no estaban disponibles porque el proyecto usa `material-icons-core`. En esos casos se utilizaron iconos disponibles con un significado visual parecido para no agregar una dependencia innecesaria.

### Conclusiones

- Aprendí que separar los modelos, el repositorio y la navegación ayuda a que las pantallas se puedan cambiar visualmente sin romper la lógica de las citas.
- Comprendí mejor que en Compose los cambios de estado provocan recomposición. Por eso la búsqueda y los horarios pueden cambiar automáticamente cuando cambia el texto o el día seleccionado.
- La IA ayudó a avanzar más rápido con estructuras de pantallas y componentes, pero fue necesario revisar el código, probarlo y corregir incompatibilidades de API, tamaños de pantalla e integración entre rutas.
- La parte más importante no fue solamente hacer que compilara, sino comprobar que la fecha seleccionada, los horarios reservados y la cita confirmada siguieran usando los mismos datos en todas las pantallas.
