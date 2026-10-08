# PROMPTS - Clínica SaludPlus

## Prompt: Calendario dinámico (Fase 2 - Pantalla 6)

Implementar el calendario dinámico de la Pantalla 6 (Fecha y hora) usando java.time.LocalDate.

Debe cumplir:

- Mostrar los próximos 5 días hábiles a partir de hoy.
- No mostrar sábados ni domingos.
- No mostrar días pasados.
- Agregar flechas < y > para avanzar y retroceder una semana.
- No permitir retroceder antes de la semana actual.
- Mostrar dinámicamente el mes y año de la semana mostrada.
- Al cambiar de día, recalcular automáticamente los horarios disponibles.
- Al cambiar de día, reiniciar la hora seleccionada.
- Mantener el bloqueo de horarios que ya están reservados.
- No romper ninguna funcionalidad de la Fase 1.
- Mantener funcionando la navegación hacia ConfirmarCitaScreen.
- Mantener funcionando el botón para volver.

## Resumen de lo realizado

- `Repositorio.kt`: se reemplazó la lista fija `fechasDisponibles` por funciones con
  `java.time.LocalDate`: `semanaActual()` (lunes de la semana actual), 
  `diasHabilesDeLaSemana()` (filtra sábados, domingos y días pasados),
  `formatearFecha()` (etiqueta "Lun 13/10") y `nombreMesYAnio()` (mes/año en español
  de la semana mostrada, p. ej. "Octubre de 2026").
- `FechaHoraScreen.kt`: la parrilla de fechas ahora se genera dinámicamente desde la
  semana mostrada; se agregaron flechas < y > para cambiar de semana (la flecha <
  se deshabilita en la semana actual); el título muestra mes y año dinámicamente;
  al cambiar de día se recalculan los horarios y se reinicia la hora seleccionada;
  al cambiar de semana se limpia la selección si el día ya no está visible.
- La clave de fecha sigue siendo el texto "Lun 13/10", por lo que el bloqueo de
  horarios reservados (`horariosDisponibles`), `agendarCita`, ConfirmarCitaScreen,
  MisCitasScreen y el botón de volver siguen funcionando igual.
- `build.gradle.kts`: `minSdk` de 24 a 26 porque `java.time` requiere API 26+
  (sin agregar librerías de desugaring).
- Tests unitarios: se actualizó el test de horarios reservados para usar la fecha
  dinámica y se agregó un test que verifica que los días hábiles excluyen fines de
  semana y días pasados.

## Correcciones realizadas

- Warning de compilación por el constructor `Locale(String)` deprecado en Java:
  se cambió a `Locale.of("es")`.
- El test unitario usaba la lista fija `fechasDisponibles` (eliminada): se cambió
  a `formatearFecha(LocalDate.now())`.

---

## Prompt: Mejora de diseño de la Pantalla 6 (Fecha y hora)

Mejorar completamente el diseño visual de la Pantalla 6 (Fecha y hora) sobre el
calendario dinámico existente, con un diseño moderno, profesional y atractivo,
como una aplicación real de citas médicas: encabezado, título, mes/año, flechas,
selector de días, estados seleccionados/deshabilitados, horarios disponibles y
ocupados, botón continuar, espaciado, tipografía, cards, bordes y formas.
Mantener la identidad visual de SaludPlus, sin librerías innecesarias y sin romper
la funcionalidad del calendario dinámico ni la navegación.

## Resumen de lo realizado (diseño)

- La pantalla pasó de dos `LazyVerticalGrid` con opciones simples a un diseño con
  scroll vertical y secciones diferenciadas:
  - Encabezado con subtítulo "Elige el día y la hora de tu cita".
  - Card elevada con bordes redondeados (24 dp) que contiene la navegación del
    mes: flechas < y > en botones circulares con borde, mes/año centrado en
    `titleLarge` semibold y rango de fechas de la semana ("Del 13 al 17").
  - Selector de días con tarjetas de 64x92 dp: abreviatura del día, número
    grande, indicador "Hoy" para el día actual, fondo `primary` animado con
    `animateColorAsState` al seleccionar y borde sutil cuando no está seleccionada.
  - Sección de horarios con título semibold y chip con la fecha seleccionada;
    horarios como chips redondeados: disponibles con borde `primary`, seleccionado
    relleno `primary`, ocupados en `surfaceVariant` deshabilitados y con texto
    tachado (`TextDecoration.LineThrough`) para mostrar visualmente la ocupación.
  - Estados vacíos como cards suaves con icono y mensaje.
  - Resumen "Cita: Lun 13/10 · 09:00" en `primaryContainer` antes de continuar.
  - Botón continuar reutilizando `BotonPrincipal`.
- Composables privados en la misma pantalla: `TarjetaDia`, `ChipHora`, `EstadoVacio`.
- Toda la lógica del calendario dinámico se mantuvo igual (cambio de semana,
  selección de día, recálculo de horarios, reinicio de hora, bloqueo de
  reservados, botón volver y navegación a ConfirmarCitaScreen).

## Correcciones realizadas (diseño)

- Ninguna de compilación: `compileDebugKotlin` y `testDebugUnitTest` pasaron a la
  primera (BUILD SUCCESSFUL, 3/3 tests).

---

## Prompt: Integración de la fecha dinámica con ConfirmarCitaScreen (Fase 3)

Integrar correctamente la fecha dinámica de la Pantalla 6 con la Pantalla 7
(ConfirmarCitaScreen), en el flujo FechaHoraScreen → ConfirmarCitaScreen →
CitaExitosaScreen. La fecha seleccionada debe llegar correctamente y mostrarse en
ConfirmarCitaScreen en español con formato largo como "Martes 16 de setiembre
2026" (día de la semana, día del mes, mes en español y año), correspondiendo
exactamente con el día seleccionado, manteniendo el horario, el bloqueo de
reservados, la confirmación, CitaExitosaScreen y el botón volver.

## Resumen de lo realizado (integración)

- El problema de fondo: la navegación pasaba la etiqueta compacta "Lun 13/10"
  (sin año), por lo que ConfirmarCitaScreen no podía reconstruir la fecha larga
  de forma confiable. Se integró la fecha canónica `LocalDate` (ISO, "2026-10-13")
  como clave del flujo:
  - `FechaHoraScreen`: `fechaSeleccionada` ahora es `LocalDate?`; se envía
    `fecha.toString()` (ISO) a la navegación; la clave interna para
    `horariosDisponibles` también es ISO (el bloqueo de reservados sigue
    funcionando porque `agendarCita` usa la misma clave); las etiquetas
    visibles ("Lun 13/10") se derivan con `formatearFecha` solo para mostrar.
  - `Repositorio`: nuevo `fechaLarga(fecha)` que genera "Martes 13 de octubre
    2026" con día de la semana y mes en español (variante "setiembre"),
    sin librerías adicionales.
  - `ConfirmarCitaScreen`: recibe la fecha ISO, la parsea con
    `LocalDate.parse` (con fallback al texto crudo si el parseo falla) y
    muestra `fechaLarga` en la tarjeta "Fecha".
- La confirmación (`agendarCita`), CitaExitosaScreen, el botón volver y Mis
  Citas siguen funcionando sin cambios de código.

## Correcciones realizadas (integración)

- El test unitario usaba la etiqueta compacta como clave de `horariosDisponibles`;
  se actualizó a la clave ISO (`LocalDate.now().toString()`).
- Se agregó un test (`fechaLargaMuestraDiaYMesEnEspanol`) que valida el formato
  exacto en español, incluida la variante "setiembre".
- Compilación y tests: BUILD SUCCESSFUL, 4/4 tests verdes.

---

## Prompt: Revisión final de la Fase 2

Revisar todo lo implementado en la Fase 2 y dejar SaludPlus listo para entregar:
verificar LocalDate, próximos 5 días hábiles, exclusión de sábados, domingos y
días pasados, flechas de semana, límite de retroceso, mes/año dinámico, recálculo
y reinicio de horarios, bloqueo de reservados, integración con ConfirmarCitaScreen
y fecha en español, flujo de confirmación, CitaExitosaScreen, botón volver,
diseño profesional y que no se rompiera nada de la Fase 1. Compilar y corregir
únicamente problemas de la Fase 2.

## Resumen de lo realizado (revisión final)

- Checklist de 18 requisitos verificado leyendo el código de `Repositorio.kt`,
  `FechaHoraScreen.kt`, `ConfirmarCitaScreen.kt`, `CitaExitosaScreen.kt`,
  `AppNavigation.kt` y `Rutas.kt`: todos cumplidos.
- Build limpio completo (`gradlew clean assembleDebug testDebugUnitTest`):
  BUILD SUCCESSFUL, APK de debug generado, 4/4 tests unitarios verdes.

## Correcciones realizadas (revisión final)

- Bug de runtime: `nombreMesYAnio` usaba `Locale.of("es")`, API disponible solo
  desde Android 16 (API 36); con `minSdk 26` fallaría en dispositivos anteriores.
  Se reemplazó por un helper privado `mesEnEspanol()` compartido con `fechaLarga`
  (sin `DateTimeFormatter` ni `Locale`), lo que además unifica la variante
  "setiembre" en el encabezado del calendario.
- Bug de layout: la fila de 5 tarjetas de día (360dp fijos) desbordaba en
  pantallas de 360dp de ancho; se agregó `horizontalScroll` a la fila.
- Se extendió el test de formato con aserciones de `nombreMesYAnio`
  ("Setiembre de 2026" / "Octubre de 2026").
