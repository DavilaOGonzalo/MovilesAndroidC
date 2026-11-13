# Registro de uso de IA

## Consulta 1 — Fecha: 2026-11-13

Herramienta: Codex (GPT-6), asistente de programación.

Prompt (resumen fiel): Implementar desde cero PokemonFire en Kotlin/Compose según el laboratorio adjunto; cubrir PokéAPI, MVVM, Room, Firebase Auth/Firestore, favoritos, offline, notificaciones y panel Admin; usar la rama autorizada `codigo-mio`, hacer commits progresivos y detenerse al llegar a la configuración manual de Firebase.

Qué respondió (resumen): Inspeccionó el Word, identificó que la carpeta estaba vacía, preparó el proyecto y desarrolló la base funcional antes del paso externo de Firebase.

Qué usamos / qué cambiamos y por qué: Se usó la estructura, los contratos y las versiones del laboratorio. Se adaptó `Item` a `Pokemon` con ID de nombre; se separaron modelos API, Room y dominio. El proyecto usa `codigo-mio` por instrucción explícita del usuario, aunque el Word describe ramas de fases distintas.

Qué aprendimos: La caché debe conservar los favoritos al sincronizar. Firebase necesita un proyecto y configuración creados por el equipo antes de probar login real.

## Consulta 2 — Fecha: 2026-11-13

Herramienta: Codex (GPT-6), asistente de programación.

Prompt (resumen fiel): Después de colocar `google-services.json` en `app/`, aplicar Google Services en Gradle, verificar Firebase Authentication y Cloud Firestore, compilar y corregir errores sin cambiar la arquitectura, rama ni publicar cambios.

Qué respondió (resumen): Validó el JSON sin mostrar su contenido, confirmó el paquete Android, aplicó el plugin, resolvió Firebase y ejecutó la compilación y las pruebas.

Qué usamos / qué cambiamos y por qué: Se aplicó `com.google.gms.google-services` al módulo, manteniendo su versión 4.4.2 declarada en el proyecto. Se conservó el archivo JSON fuera del control de versiones.

Qué aprendimos: La configuración local de Firebase procesó el archivo correctamente y la app compila con Auth y Firestore.

## Consulta 3 — Fecha: 2026-11-13

Herramienta: Codex (GPT-6), asistente de programación.

Prompt (resumen fiel): Continuar Firebase Authentication y Cloud Firestore después de habilitar Email/Password, crear cuentas y documentos de roles; probar ambos inicios de sesión, corregir errores, compilar y probar, sin push ni cambio de rama.

Qué respondió (resumen): Revisó el mapeo de `usuarios/{uid}` a los roles, ajustó la cancelación de operaciones Firebase, recompiló y abrió la app en el emulador para la validación manual de las dos cuentas.

Qué usamos / qué cambiamos y por qué: La comprobación de credenciales se hace en el emulador; las contraseñas no se copian al chat. La prueba de ambos roles queda pendiente de confirmar después de que el usuario complete los formularios.

Qué aprendimos: La compilación no prueba las credenciales reales ni los documentos de Firestore; hay que comprobar ADMIN y USUARIO en el dispositivo.
