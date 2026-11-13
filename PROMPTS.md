# Registro de uso de IA

## Consulta 1 — Fecha: 2026-11-13

Herramienta: Codex (GPT-6), asistente de programación.

Prompt (resumen fiel): Implementar desde cero PokemonFire en Kotlin/Compose según el laboratorio adjunto; cubrir PokéAPI, MVVM, Room, Firebase Auth/Firestore, favoritos, offline, notificaciones y panel Admin; usar la rama autorizada `codigo-mio`, hacer commits progresivos y detenerse al llegar a la configuración manual de Firebase.

Qué respondió (resumen): Inspeccionó el Word, identificó que la carpeta estaba vacía, preparó el proyecto y desarrolló la base funcional antes del paso externo de Firebase.

Qué usamos / qué cambiamos y por qué: Se usó la estructura, los contratos y las versiones del laboratorio. Se adaptó `Item` a `Pokemon` con ID de nombre; se separaron modelos API, Room y dominio. El proyecto usa `codigo-mio` por instrucción explícita del usuario, aunque el Word describe ramas de fases distintas.

Qué aprendimos: La caché debe conservar los favoritos al sincronizar. Firebase necesita un proyecto y configuración creados por el equipo antes de probar login real.
