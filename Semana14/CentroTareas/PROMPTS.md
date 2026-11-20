# PROMPTS.md — CentroTareas Fase 2

Registro de prompts de esta sesión y decisiones implementadas. **Gemini no se utilizó**: las interacciones de IA documentadas aquí fueron con Codex (ChatGPT), en respuesta al encargo del usuario. No se atribuyen respuestas ficticias a Gemini.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Implementar `withTimeout` en las cargas. Si una carga supera los 2 segundos, mostrar `Tardó demasiado`. Manejar `TimeoutCancellationException` sin confundirla con cancelación normal.» | Codex señaló que las cargas simuladas de 2 s y 3 s excederían o alcanzarían el límite y que ese resultado sería esperado. Implementó `withTimeout(2_000)` y maneja `TimeoutCancellationException` antes de `CancellationException`. | Se conserva el límite exacto pedido; el timeout se aplica a cada intento de la operación completa y produce un estado de error visible. | Mantiene el contrato del laboratorio y evita dejar la pantalla en estado Cargando. |
| «Reintentar cargas hasta tres veces con espera creciente. Respetar `CancellationException` y volver a lanzarla.» | Codex propuso reintentar tanto timeout como fallos recuperables y relanzar una cancelación normal. | Se implementan tres reintentos después del intento inicial, con demoras de 500, 1000 y 1500 ms. Timeout se captura por separado; una cancelación normal se relanza de inmediato. | Las esperas son crecientes, acotadas y no bloquean el hilo principal. |
| «Crear una tabla o sección visual que acumule los tiempos de Una por una y A la vez, y mantener datos/estados en ViewModel.» | Codex añadió un modelo de registro y una lista acumulada en `StateFlow`; la pantalla consume esa lista y presenta modo, duración y resultado. | Se registra una fila por ejecución completada o agotada por timeout; las ejecuciones canceladas no se registran como resultado. | La comparación queda visible y conserva el resultado real, incluidos errores/timeout, sin mover lógica de negocio a Compose. |
| «No inventes conversaciones con Gemini; documenta únicamente las interacciones reales y decisiones efectivamente tomadas.» | Codex indicó que no se había usado Gemini y registró la herramienta real usada en esta sesión. | No se escribieron respuestas atribuidas a Gemini. La columna de respuesta aclara que respondió Codex. | Mantener trazabilidad académica y evitar atribuir contenido a una herramienta que no participó. |

## Decisiones verificadas

- No se añadió dependencia nueva para timeout/reintentos/comparación; se reutilizaron Kotlin Coroutines, StateFlow y Material 3 que ya tenía el proyecto.
- El timeout se muestra como `Tardó demasiado`; otras excepciones conservan el mensaje de error existente.
- Las pruebas de unidad usan un `TestDispatcher` y repositorios falsos para comprobar cuatro intentos totales (inicial + tres reintentos) y la recuperación ante fallos transitorios.
- La fase 1 no se modificó al desarrollar las mejoras; la rama `codigo-ia` recibió los nueve commits de CentroTareas por cherry-pick y conserva su historial anterior.
