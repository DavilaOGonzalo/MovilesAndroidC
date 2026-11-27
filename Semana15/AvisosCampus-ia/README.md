# AvisosCampus

Aplicación Android local en Kotlin, Jetpack Compose y Material 3. Conserva los avisos, el permiso, el recordatorio de 10 segundos, el modo avión y el diseño de la Fase 1. La Fase 2 añade un canal Urgentes para recordatorios, un receptor independiente del cargador y un historial persistente mediante Room. No requiere internet. Proyecto en `Semana15/AvisosCampus-ia`, rama `codigo-ia`, basado en `84a1280`. El paquete de código es `com.davila.avisoscampus`; el identificador de instalación es `com.davila.avisoscampus.ia` para poder instalar ambas fases en Pixel_8.

## Estructura

```text
app/src/main/java/com/davila/avisoscampus/
├── AvisosApp.kt                 Application, canales y Repository
├── MainActivity.kt              Entrada de Compose
├── model/Modelos.kt             Aviso, Origen y PermisoUi
├── data/AvisosRepository.kt     Interfaz y adaptación Room
├── data/local/
│   ├── AvisoEntity.kt
│   ├── AvisosDao.kt
│   └── AvisosDatabase.kt
├── notificacion/Notificador.kt  Avisos, Urgentes y NotificationCompat
├── receiver/ModoAvionReceiver.kt
├── receiver/CargadorReceiver.kt
├── viewmodel/
│   ├── AvisosViewModel.kt
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt
    ├── HistorialAvisos.kt
    └── theme/Theme.kt
```

## Arquitectura MVVM

```mermaid
flowchart LR
    U[Usuario] --> V[PantallaAvisos]
    V -->|acciones| VM[AvisosViewModel]
    VM -->|registrar| R[AvisosRepository, interface]
    R --> M[AvisosRepositoryRoom]
    M --> D[AvisosDao]
    D --> DB[(Room: avisos.db)]
    R -->|StateFlow de avisos| VM
    VM -->|StateFlow de estado| V
    VM -->|SharedFlow de avisos| V
    V -->|mostrar con Context| N[Notificador]
    N --> S[Sistema Android]
    S -->|modo avión| A[ModoAvionReceiver]
    S -->|cargador| C[CargadorReceiver]
    A -->|callback| V
    C -->|callback| V
```

El ViewModel no utiliza `Context`, `NotificationManager` ni `BroadcastReceiver`. La pantalla registra cada receptor solo mientras su switch está activo y lo libera en `onDispose`. `Avisos` usa importancia normal y `Urgentes` usa `IMPORTANCE_HIGH` para recordatorios. Room conserva el historial tras cerrar y abrir la app.

## Capturas reales

Obtenidas en el emulador Pixel_8 con Android API 35. Las cinco capturas siguientes proceden del proyecto base de Fase 1; las capturas `ia-` fueron tomadas durante las pruebas de Fase 2.

### Notificación en el panel

![Avisos Tarea lista en el panel de Android](capturas/notificacion-panel.png)

### Recordatorio

![Notificación Recordatorio después de diez segundos](capturas/recordatorio.png)

### Modo avión

![Switch y aviso de cambio del modo avión](capturas/modo-avion.png)

### Historial

![Historial con origen, título, texto y hora](capturas/historial.png)

### Permiso denegado

![Estado denegado y aviso guardado sin notificación](capturas/permiso-denegado.png)

### Evidencias de Fase 2

| Función | Captura real en Pixel_8 |
| --- | --- |
| Canales Avisos y Urgentes | ![Canales](capturas/ia-canales.png) |
| Recordatorio en panel | ![Urgentes](capturas/ia-urgente.png) |
| Cargador conectado y desconectado | ![Cargador](capturas/ia-cargador.png) |
| Switch de cargador desactivado | ![Cargador desactivado](capturas/ia-cargador-switch-off.png) |
| Historial tras cerrar y abrir | ![Room persistente](capturas/ia-room-persistente.png) |
| Modo avión | ![Modo avión IA](capturas/ia-modo-avion.png) |
| Permiso denegado | ![Permiso denegado IA](capturas/ia-permiso-denegado.png) |
| Cuenta regresiva y rotación | ![Cuenta](capturas/ia-recordatorio-cuenta.png) ![Rotación](capturas/ia-rotacion-recordatorio.png) |

## Pruebas de aceptación

Las 13 pruebas originales se repitieron en la aplicación de Fase 2. Las capturas sin prefijo `ia-` documentan la ejecución previa de Fase 1; la tabla refleja las observaciones de Fase 2.

| N.º | Prueba | Resultado observado |
| --- | --- | --- |
| 1 | Canal en Ajustes | OK: aparecen «Avisos» y «Urgentes»; el segundo tiene importancia 4 (`HIGH`). |
| 2 | Solicitar permiso | OK: apareció el diálogo de Android y cambió la etiqueta. |
| 3 | Mostrar aviso | OK: apareció «Tarea lista». |
| 4 | Dos avisos | OK: se observaron dos notificaciones con identificadores distintos. |
| 5 | Tocar notificación | OK: abrió MainActivity y desapareció la notificación tocada. |
| 6 | Recordatorio de 10 s | OK: hubo cuenta regresiva y llegó «Recordatorio». |
| 7 | Minimizar durante cuenta | OK: llegó el aviso con la app minimizada. |
| 8 | Cancelar recordatorio | OK: tras cancelar y esperar 11 s no apareció un aviso nuevo. |
| 9 | Switch activo y modo avión | OK: se registraron «Activado» y «Desactivado». |
| 10 | Switch inactivo y modo avión | OK: no se añadieron avisos tras los cambios. |
| 11 | Permiso denegado | OK: mensaje visible, aviso en historial y app abierta. |
| 12 | Rotar durante la cuenta | OK: la cuenta continuó en horizontal; [captura de Fase 2](capturas/ia-rotacion-recordatorio.png). |
| 13 | Revisión de arquitectura | OK: no hay `Context` en `viewmodel/` y ambos registros tienen `unregisterReceiver` en `onDispose`. |

Pruebas adicionales: el cargador registró conexión y desconexión solo con su switch activo; al desactivarlo no se añadieron eventos. El historial siguió visible después de `force-stop` y reapertura. Compilación comprobada con `./gradlew :app:assembleDebug`.

## Para pensar

1. **¿Por qué canal y permiso?** En Android 8+ el canal clasifica las notificaciones y permite al usuario controlar su importancia; sin canal, una notificación destinada a él no se muestra. En Android 13+ también se necesita autorización `POST_NOTIFICATIONS`; si se deniega, la app no puede publicar avisos normales, aunque sí conservarlos en su historial.
2. **¿Por qué el ViewModel emite un evento?** Decide el contenido y lo registra sin depender del sistema Android. La pantalla recibe el evento y usa `Context` para notificar, lo que mantiene el ViewModel independiente y comprobable.
3. **¿Qué ocurre si se repite un id?** `notify` actualiza o reemplaza la notificación anterior con ese id; Room genera identificadores distintos para mantener avisos separados.
4. **¿Por qué `onReceive` corto y liberar el receptor?** El sistema espera que el callback termine pronto; el trabajo largo puede bloquear la app. Un receptor registrado que no se libera mantiene referencias innecesarias y puede recibir eventos fuera de la vida útil de la pantalla.
5. **¿Cuándo local y cuándo push?** Una local sirve para eventos calculados en el propio dispositivo, como el recordatorio o el modo avión. Una push de Firebase sirve para mensajes originados en un servidor, por ejemplo un aviso nuevo del campus para varios usuarios; requiere red e infraestructura externa.
