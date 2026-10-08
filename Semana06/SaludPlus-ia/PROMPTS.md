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
