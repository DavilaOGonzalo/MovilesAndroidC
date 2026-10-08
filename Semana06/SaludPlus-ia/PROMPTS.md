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

---

## Prompt: Mejora general de diseño visual de SaludPlus

Mejorar completamente el diseño visual y la estructura de la aplicación para que
se vea como una app profesional de citas médicas: revisar todas las pantallas
(Splash, Login, Registro, Home, Especialidades, Médicos, FechaHora, ConfirmarCita,
CitaExitosa, MisCitas, Perfil), componentes reutilizables, tema, colores,
tipografía, espaciados, estados y navegación. Paleta de salud y tecnología,
identidad visual consistente, jerarquía clara, sin librerías innecesarias, sin
romper lógica de negocio ni navegación. Conservar el calendario dinámico y el
bloqueo de horarios en FechaHoraScreen y la fecha en español en ConfirmarCitaScreen.

## Resumen de lo realizado (diseño general)

- Tema propio (se desactivó el color dinámico para identidad consistente):
  `Color.kt` con paleta médica teal/azul (claro y oscuro), `Type.kt` con escala
  tipográfica completa y `Theme.kt` con ambos esquemas.
- Componentes reutilizables mejorados/creados en `ui/components`:
  - `BotonPrincipal`: esquinas de 16 dp, alto 52 dp, estilo labelLarge.
  - `BotonSecundario` (nuevo): outlined, con variante de error para
    "Cerrar sesión".
  - `CampoTexto`: esquinas de 14 dp, icono leading opcional (email, candado,
    persona, búsqueda).
  - `EncabezadoConVolver`: botón de volver en círculo surfaceVariant, título
    titleLarge.
  - `TarjetaSeccion`: card de 18 dp con icono circular opcional y chevron de
    navegación opcional (parámetros `icono` y `flecha`).
  - `TarjetaEspecialidad`: avatar circular con inicial, clickable, 210 dp.
  - `TarjetaMedico`: avatar con icono persona, especialidad, estrella ámbar de
    calificación, experiencia y chevron.
  - `EstadoVacio` (nuevo, compartido): usado en FechaHora, Especialidades,
    Médicos y Mis Citas (se eliminó el duplicado privado de FechaHoraScreen).
- Pantallas rediseñadas manteniendo toda la lógica:
  - Splash: fondo primary, logo de corazón, nombre y subtítulo.
  - Login/Registro: encabezado de marca, iconos en campos, mensajes de error en
    pastilla errorContainer, mismas validaciones.
  - Términos: encabezado con volver, texto en card, botón "Entendido".
  - Home: saludo + avatar, card CTA "Agendar cita" que navega a especialidades,
    destacadas con tarjetas nuevas, accesos rápidos con iconos y chevrones.
  - Especialidades/Médicos: buscador con icono, estados vacíos, tarjetas
    modernas; en Médicos, chip con el nombre de la especialidad.
  - ConfirmarCita: encabezado con volver, card de detalles con filas
    etiqueta/valor y divisores, fecha larga en español conservada, botón
    "Confirmar cita" con la misma lógica.
  - CitaExitosa: check grande en círculo primaryContainer, texto de apoyo.
  - MisCitas: encabezado, estado vacío, tarjetas con fecha larga + hora.
  - Perfil: avatar, nombre/email, datos en tarjetas, cerrar sesión outlined en
    color error.
  - Resultados: placeholder con icono y mensaje.
- Navegación, rutas, modelos y Repositorio sin cambios.

## Correcciones realizadas (diseño general)

- Error de compilación: faltaba el import de `dp` en `CampoTexto.kt`.
- Error de compilación: faltaba el import de `Surface` en `TarjetaSeccion.kt`
  (y se simplificó el contenedor del icono).
- Warning deprecado por `Icons.Filled.List` en HomeScreen: migrado a
  `Icons.AutoMirrored.Filled.List`.
- Se eliminó un import sin uso en `EspecialidadesScreen`.
- Build limpio final: `clean assembleDebug testDebugUnitTest` → BUILD SUCCESSFUL,
  APK generado, 4/4 tests verdes.

---

## Prompt: Mejora creativa de diseño de SaludPlus

Mejora el diseño creativo de toda la aplicación SaludPlus para que se vea más
moderna y colorida: degradados, tarjetas modernas, bordes redondeados, sombras,
chips, badges, muchos colores amigables y avatares con fotos profesionales de los
médicos. No agregar librerías innecesarias, no romper la lógica de calendario,
navegación ni el bloqueo de horarios reservados. Actualizar PROMPTS.md y hacer
commit ("Mejora diseño creativo de SaludPlus"), sin push.

## Resumen de lo realizado (diseño creativo)

- Avatares: como el proyecto no tiene fotos de médicos reales y no se agregan
  librerías de imágenes de red, se creó `AvatarInicial`: avatar circular con
  degradado generado por nombre (`gradienteAvatar`, `Color.hsv`) y las dos
  iniciales del médico. Se usa en TarjetaMedico, ConfirmarCita, MisCitas,
  CitaExitosa y Perfil.
- Nuevos componentes en `ui/components`:
  - `Estilos.kt`: `gradienteMarca()` (primary→tertiary), `gradienteExito()`,
    `gradienteAvatar(nombre)` y `estiloEspecialidad(id)` que asigna color e
    icono por especialidad (Cardiología rojo/pulmón, Pediatría naranja/rostro,
    Dermatología morado/estrella, Traumatología café/herramienta, Neurología
    índigo/info, Medicina general teal/buscar).
  - `BotonGradiente.kt`: botón 52 dp con degradado de marca, sombra y estado
    deshabilitado; usado en Login, Registro, FechaHora, ConfirmarCita y
    CitaExitosa.
  - `TarjetaMedicoDestacado.kt`: tarjeta compacta 160x150 para el carrusel de
    médicos destacados del Home.
  - `TarjetaSeccion`: nuevos parámetros `colorIcono` y `etiqueta` (pastilla de
    color) además de `icono` y `flecha`.
  - `TarjetaEspecialidad` y `TarjetaMedico`: colores por especialidad y chip de
    especialidad tintado; TarjetaMedico con avatar de inicial y badge de
    calificación en pastilla ámbar.
- Pantallas:
  - Splash/Login/Registro: fondos con degradado de marca (cabecera redondeada
    en Login/Registro), logo en círculo blanco con sombra.
  - Home: cabecera con degradado (saludo + avatar persona + CTA "Agendar cita"
    en cristal), carrusel de destacadas coloridas y de médicos destacados,
    accesos rápidos con iconos de colores.
  - Especialidades: tarjetas con color e icono por especialidad.
  - Médicos: héroe con degradado de marca, nombre y cantidad de médicos.
  - FechaHora (solo visual, lógica intacta): tarjeta de mes tintada
    primaryContainer, día seleccionado con sombra, chip de fecha en
    tertiaryContainer, chips de hora con sombra al seleccionar, resumen con
    `animateContentSize` y botón degradado (mismo `enabled`).
  - ConfirmarCita: héroe del médico con avatar de inicial, chip de
    especialidad colorida, calificación/experiencia, filas de detalle con
    iconos en recuadros tintados (sin divisores), botón degradado;
    `agendarCita(medicoId, fecha, hora)` sin cambios.
  - CitaExitosa: fondo con degradado suave, check en círculo con
    `gradienteExito` y sombra, card con el resumen de la última cita.
  - MisCitas: tarjetas con avatar, chip de estado "Confirmada" (fecha futura,
    con check) o "Completada" derivado comparando la fecha ISO con
    `LocalDate.now()`, pastilla de fecha en el color de la especialidad.
  - Perfil: cabecera con degradado, avatar de inicial del usuario, datos en
    tarjetas con iconos de colores.
  - Resultados: fondo con degradado tertiaryContainer, icono en círculo
    blanco con sombra.
- Navegación, rutas, modelos, Repositorio y lógica de negocio sin cambios.

## Correcciones realizadas (diseño creativo)

- Error de compilación: `AvatarInicial.kt` no importaba `Color`.
- Error de compilación: referencia totalmente calificada inválida de
  `KeyboardArrowRight` en TarjetaMedico → import normal de
  `Icons.AutoMirrored.Filled.KeyboardArrowRight`.
- Error de compilación: `Surface` de Material3 no acepta `brush` en esta
  versión → círculo de éxito con `Box` + `clip` + `background(gradienteExito())`.
- MisCitas (dentro del bottom nav) no debe usar `EncabezadoConVolver` → se
  restauró el título simple "Mis citas".
- Build final: `compileDebugKotlin testDebugUnitTest assembleDebug` →
  BUILD SUCCESSFUL, APK generado, 4/4 tests verdes.

---

## Prompt: Mejora diseño visual de SaludPlus

Trabajar directamente en `Semana06/SaludPlus-ia` usando como referencia visual
`C:\Users\Gonzalo\Downloads\SaludPlusIMG\a9f0ba78-9e1e-4467-a10d-581fb66323de.png`.
Hacer que SaludPlus sea lo más fiel posible al mockup, manteniendo la lógica de
calendario, reservas, fotos y navegación. Compilar, corregir errores y hacer
commit con `Mejora diseño visual de SaludPlus`, sin push.

## Resumen de lo realizado (mockup)

- Se ajustó la identidad visual a fondo blanco/azul muy claro, azul principal
  `#1878F0`, texto azul marino, tarjetas blancas con bordes suaves y botones
  azules sólidos de esquinas pequeñas.
- Se creó `LogoSaludPlus` con el corazón azul y cruz blanca, y se ordenaron
  Login, Registro y Splash según el flujo de la referencia.
- Home ahora tiene barra superior con marca, saludo, búsqueda, cuatro accesos
  en grid, seis especialidades coloridas y sección de próximas citas.
- Especialidades muestra búsqueda y lista de ocho especialidades con iconos y
  colores diferenciados; Médicos añade filtros, fotos reales, reseñas y
  disponibilidad.
- Perfil médico usa fotografía grande, calificación, experiencia, clínica,
  descripción y botón para agendar.
- FechaHora conserva `LocalDate`, semanas, días hábiles, bloqueo de horarios y
  selección, pero adopta el selector compacto de la referencia.
- Se añadió `MotivoLugarScreen` y su ruta para replicar el paso intermedio del
  mockup. La confirmación conserva `Repositorio.agendarCita` y ahora muestra
  motivo, fecha, hora y lugar.
- CitaExitosa, MisCitas, Perfil, Reportes y navegación inferior fueron
  uniformados con el mismo lenguaje visual.
- Usuario admite teléfono y Médico admite cantidad de reseñas sin romper los
  constructores existentes. Los médicos y especialidades se actualizaron con
  nombres y categorías visibles en la referencia.

## Verificación

- `compileDebugKotlin testDebugUnitTest assembleDebug` → BUILD SUCCESSFUL.
- Tests unitarios: 4/4 verdes.
- APK generado en `app/build/outputs/apk/debug/app-debug.apk`.

---

## Prompt: Ajusta interfaz según referencia visual

Ajusta toda la interfaz de SaludPlus siguiendo la referencia visual adjunta:
paleta azul médico, fondo claro, tarjetas redondeadas, iconos grandes y coloridos,
fotos reales de médicos, foto real del usuario logueado, barra de navegación
inferior, espaciado amplio, sin pantallas apretadas ni vacías. Rediseñar tema,
Login, Registro, Home, Especialidades, Médicos, ficha de médico, FechaHora,
ConfirmarCita, CitaExitosa, MisCitas, Perfil y Resultados. No romper la lógica
del calendario dinámico ni el bloqueo de horarios. Actualizar PROMPTS.md y hacer
commit ("Ajusta interfaz según referencia visual"), sin push.

> Nota: la imagen de referencia no quedó accesible en el entorno (se buscó en
> `C:\Users\Gonzalo\Desktop` y no se encontró archivo de imagen). El rediseño se
> basó en la descripción textual del prompt.

## Resumen de lo realizado (referencia visual)

- Tema: `Color.kt`/`Theme.kt` con nueva paleta azul médico (`BluePrimary
  0xFF1E6BD6`, terciario cian, ámbar para calificación, verde para
  disponibilidad), fondo claro (`0xFFF5F8FD`) y superficies blancas; esquemas
  claro y oscuro completos.
- `Estilos.kt`: `gradienteMarca` azul→cian, nuevo `gradienteEncabezado`
  azul→azul claro, `gradienteExito` verde y helper `colorDisponibilidad`.
- `BarraNavegacionInferior`: `NavigationBar` blanca con icono/texto e indicador
  de color distinto por destino.
- `Repositorio`: datos de clínica (`clinicaNombre`, `clinicaDireccion`) y
  `proximaDisponibilidad(medicoId)` que recorre hasta 10 días hábiles usando
  `horariosDisponibles` (sin cambiar la lógica existente).
- Nuevo `PerfilMedicoScreen` (ficha del médico): foto grande, nombre,
  especialidad, disponibilidad, tarjetas de calificación/experiencia, sección
  "Sobre el especialista" con enfoque y centro de atención, y botón "Agendar
  cita" que navega a FechaHora. Ruta `FICHA_MEDICO` en `Rutas`/`AppNavigation`.
- Home: cabecera con degradado azul (saludo + foto de usuario), barra de
  búsqueda, CTA "Agendar cita", especialidades destacadas centradas, tarjeta
  "Próxima cita" (o acceso para agendar) y carrusel de médicos destacados que
  abre la ficha del médico.
- Especialidades: encabezado, buscador y chips de filtro "Todas"/"Destacadas";
  tarjetas con icono de color y etiqueta "Destacada".
- Médicos: tarjetas grandes con foto real, especialidad, calificación y
  disponibilidad (verde/neutro) que abren la ficha del médico.
- Login/Registro/Perfil: cabeceras de marca ordenadas, campos centrados y
  botones amplios separados; estilo uniforme.
- ConfirmarCita: card del médico con foto real y fila de detalle "Clínica".
- CitaExitosa: icono grande, mensaje, resumen y dos botones separados
  ("Ver mis citas" + "Ir al inicio").
- Fotos reales ya integradas con Coil (Pexels): médicos y foto del usuario.
- Lógica de calendario, bloqueo de horarios reservados, `agendarCita`,
  navegación y fecha en español: sin cambios.

## Correcciones realizadas (referencia visual)

- `Icons.Filled.Work` no existe en `material-icons-core` → se usó
  `Icons.Filled.Build` en las tarjetas de estadística de la ficha del médico.
- Build final: `compileDebugKotlin testDebugUnitTest assembleDebug` →
  BUILD SUCCESSFUL, APK generado, 4/4 tests verdes.
