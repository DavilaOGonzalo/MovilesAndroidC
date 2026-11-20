# CentroTareas

Aplicación Android didáctica del Laboratorio 14 de Programación en Móviles. Muestra cómo mantener la interfaz disponible al ejecutar tareas demoradas, calcular primos y comparar cargas secuenciales y paralelas.

> **Base del proyecto:** no se encontró un starter de CentroTareas en Semana14, en las ramas inspeccionadas ni en las ubicaciones locales consultadas. Por eso, el esqueleto de `codigo-mio` se creó siguiendo los contratos y archivos descritos en el PDF; no se presenta como copia del proyecto entregado por el docente.
>
> **Trazabilidad:** esta implementación fue realizada con asistencia de Codex durante la sesión. `codigo-mio` conserva los pasos/commits de la Fase 1 del laboratorio, pero no sería correcto afirmar que el código se produjo sin IA.

## Tecnologías

- Kotlin y Android Gradle Plugin.
- Jetpack Compose y Material 3 (interfaz declarativa, sin layouts XML).
- MVVM con `ViewModel`, `StateFlow` y `viewModelScope`.
- Kotlin Coroutines: `withContext`, `Dispatchers.Default`, `Job`, `async`, `await`, `delay`.
- Repository (`TareasRepository`) y una implementación local con datos ficticios.
- JUnit 4 y `kotlinx-coroutines-test` para verificar el conteo de primos.

## Funcionalidades

1. Paso histórico: `tareaLargaMala()` usó `Thread.sleep(4000)` en el hilo principal para demostrar el congelamiento. La implementación se retiró en el paso siguiente.
2. `tareaLarga()` simula cuatro segundos en `Dispatchers.Default`, informa los hilos con `Log.d("HILO", ...)` y mantiene activo el contador de toques.
3. Progreso de 0 a 100 % expuesto como `StateFlow<Float>`.
4. Cancelación por `Job`, con prevención de doble inicio y estado final en `finally`.
5. Conteo cancelable de primos en `Dispatchers.Default`, comprobando cancelación en el bucle.
6. Cargas de perfil/pedidos de forma secuencial (aprox. 5 s) y paralela con `async/await` (aprox. 3 s).
7. Error simulado `IOException("Sin conexión")`, estado de error visible y reintento manual.

## Arquitectura

```text
PantallaTareas (Compose)
       │ eventos / collectAsState
       ▼
TareasViewModel ── StateFlow de estados de pantalla
       │ depende de la interfaz
       ▼
TareasRepository ◀── TareasRepositoryLocal
                         ├─ demoras ficticias
                         └─ conteo de primos cancelable
```

La View solo presenta el estado y envía eventos. El ViewModel coordina corrutinas y estado sin `Context`. El Repository realiza las cargas ficticias y el cálculo de primos. La cancelación se vuelve a lanzar para conservar la semántica cooperativa.

## Observación del Paso 1

En la versión histórica del paso 1, `Thread.sleep(4000)` se ejecutaba desde `tareaLargaMala()` en el hilo principal. Durante esos cuatro segundos Android no podía procesar toques ni volver a dibujar. El mensaje `Trabajando...` se asignaba antes de dormir, pero el siguiente frame no podía dibujarse hasta que acabara la espera; por eso el texto no llegaba a verse durante la tarea. Una espera de diez segundos probablemente activaría el aviso de que la aplicación no responde (ANR). Esta observación se describe a partir del código del paso; la captura incluida corresponde al reemplazo funcional con progreso, no al estado bloqueado.

## Capturas reales del emulador

Las imágenes se tomaron desde el emulador Android con la aplicación instalada. Los tiempos varían según el dispositivo.

### Tarea con progreso

![Progreso real](docs/capturas/tarea-con-progreso.png)

### Cancelación

![Tarea cancelada](docs/capturas/tarea-cancelada.png)

### Conteo de primos (1000 → 168)

![Resultado de primos](docs/capturas/resultado-primos.png)

### Comparación de cargas

Secuencial, alrededor de 5 s:

![Carga una por una](docs/capturas/carga-una-por-una.png)

Paralela, alrededor de 3 s:

![Carga a la vez](docs/capturas/carga-a-la-vez.png)

### Error y reintento

![Error simulado](docs/capturas/error-y-reintentar.png)

## Pruebas de aceptación

| Prueba | Resultado esperado | Verificación |
|---|---|---|
| Contador durante tarea | Sigue respondiendo | Captura de progreso en emulador |
| Hilos `HILO` | main / DefaultDispatcher-worker / main | Implementado mediante Logcat; no se archivó un log de la sesión |
| Progreso | 0–100 % en aprox. 4 s | Captura real al 36 % |
| Cancelar | Se detiene y permite reiniciar | Captura real al 50 % |
| Primos 100 | 25 | Prueba unitaria |
| Primos 1000 | 168 | Prueba unitaria y captura |
| Primos 10000 | 1229 | Prueba unitaria |
| Cargas | Secuencial ≈ 5 s, paralela ≈ 3 s | Capturas reales: 5039 ms y valor paralelo registrado en la pantalla |
| Error | Error visible; reintento disponible | Captura real de «Sin conexión» |
| Código final | Sin `GlobalScope`, `runBlocking` ni `Thread.sleep` | Revisar fuentes finales; `Thread.sleep` solo aparece en el commit histórico del paso 1 |

No se marcó como verificada la prueba de rotación ni se ejecutó la versión bloqueante para evitar dejar el emulador congelado; ambas quedan pendientes de comprobación manual en Android Studio.

## Para pensar

1. **¿Por qué no se veía Trabajando... en el Paso 1?** Porque la asignación actualiza el estado, pero el hilo principal queda bloqueado antes de que Compose pueda dibujar el siguiente frame.
2. **¿Qué diferencia hay entre Thread.sleep y delay?** `Thread.sleep` bloquea el hilo que lo ejecuta. `delay` suspende la corrutina y libera el hilo para atender otras tareas.
3. **¿Por qué Dispatchers.Default para los primos y Dispatchers.IO para la red?** El conteo es trabajo intensivo de CPU y encaja con `Default`; operaciones de entrada/salida esperan recursos externos y normalmente usan `IO`. En este laboratorio, las cargas son simuladas con `delay`, no hacen red real.
4. **¿Qué pasaría si el usuario cierra la pantalla? ¿Qué papel cumple viewModelScope?** Al destruirse el ViewModel se cancela su `viewModelScope` y las tareas cooperativas vinculadas a él; así no siguen trabajando sin dueño de UI.
5. **¿Por qué async tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?** Las dos tareas se inician a la vez y el total se aproxima a la más larga: `max(2 s, 3 s)`. En un `coroutineScope`, un fallo cancela la tarea hermana y se propaga al ViewModel, que lo presenta como error sin cerrar la app.

## Compilar y probar

En Windows, con Android SDK instalado y disponible en `ANDROID_HOME`:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.
