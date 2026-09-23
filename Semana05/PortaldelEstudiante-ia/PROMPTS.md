QUÉ VA A HACER

Trabaja sobre el proyecto ubicado en:

C:\Users\Gonzalo\Desktop\MovilesAndroidC\Semana05\PortaldelEstudiante-ia

Analiza primero la estructura y el código actual del proyecto. Después implementa el flujo completo de una aplicación académica en Kotlin con Jetpack Compose y Navigation Compose, manteniendo los componentes independientes.

Realiza los cambios directamente en el proyecto y verifica que compile correctamente.

CONTEXTO / SITUACIÓN

El proyecto ya contiene una estructura de navegación con MainActivity, AppNavigation, Screen, HomeScreen, ListScreen, DetailScreen y ProfileScreen.

MainActivity debe seguir siendo únicamente el punto de entrada de la aplicación y ejecutar AppNavigation(). La navegación debe permanecer en AppNavigation y las rutas en Screen.

La aplicación debe tomar como referencia visual las imágenes proporcionadas: una aplicación académica con predominio de tonos morados, fondos claros, tarjetas, bordes redondeados, iconos y una distribución limpia.

No reemplaces la arquitectura existente por otra diferente. Mantén Jetpack Compose, Navigation Compose y los componentes independientes.

RESULTADO FINAL ESPERADO

1. LOGIN

Crear LoginScreen como componente independiente.

Debe mostrar:
- "Portal Académico".
- Campo de correo institucional.
- Campo de contraseña.
- Opción para mostrar u ocultar la contraseña.
- Botón "Iniciar sesión".
- Opción "¿Olvidaste tu contraseña?".

Al iniciar sesión debe navegar a HomeScreen.

2. RECUPERAR CONTRASEÑA

Crear RecoverPasswordScreen como componente independiente.

Debe ser accesible desde "¿Olvidaste tu contraseña?".

Debe mostrar:
- Campo para correo institucional.
- Botón para continuar.
- Opción para regresar al Login.

No implementar autenticación real ni conexión con servicios externos. Utilizar datos estáticos.

3. INICIO

HomeScreen debe mostrar después del inicio de sesión:

- "Bienvenido, Juan León".
- Un texto indicando qué puede gestionar.
- Opción "Directorio de Alumnos".
- Opción "Mi Perfil".
- Opción "Cerrar sesión".

4. DIRECTORIO DE ALUMNOS

ListScreen debe utilizar LazyColumn.

Cada fila debe mostrar:
- Foto del alumno.
- Nombre.
- Carrera.
- Indicador para acceder al detalle.

Debe existir una lista de varios alumnos.

Al seleccionar una fila debe navegar a DetailScreen utilizando el itemId correspondiente.

5. EXPEDIENTE ACADÉMICO

DetailScreen debe recibir el itemId mediante Navigation Compose y mostrar el expediente correspondiente.

Debe incluir:
- Foto del alumno.
- Nombre.
- Carrera.
- ID del estudiante.
- Correo electrónico.
- Facultad.
- Biografía.

6. PERFIL

ProfileScreen debe mostrar el perfil del usuario que inició sesión.

Debe incluir:
- Foto.
- Nombre completo.
- Correo.
- Teléfono.
- Carrera.
- Ciclo actual.
- Sección de información personal.
- Sección de información académica.
- Botón "Cerrar sesión".

7. CERRAR SESIÓN

Implementar un método de cierre de sesión accesible desde HomeScreen y ProfileScreen.

Al cerrar sesión:
- Regresar a LoginScreen.
- Limpiar el back stack de las pantallas autenticadas.
- Impedir que el usuario vuelva a HomeScreen, ListScreen o ProfileScreen utilizando el botón atrás.

8. DISEÑO

Aplicar a todas las pantallas un diseño coherente con las imágenes de referencia:

- Predominio de tonos morados.
- Fondo claro.
- Tarjetas con bordes redondeados.
- Espaciado uniforme.
- Iconos relacionados con cada sección.
- Jerarquía visual clara.
- Diseño académico, limpio y moderno.

Para las fotos de alumnos, utiliza recursos de imagen existentes en el proyecto si están disponibles. Si no existen, crea una solución local que permita mostrar diferentes imágenes/avatar sin depender de una API o servidor externo.

REGLAS

- Mantener MainActivity como punto de entrada.
- Mantener AppNavigation para la navegación.
- Mantener Screen para las rutas.
- Cada pantalla debe estar en su propio archivo.
- No crear una Activity por pantalla.
- No colocar las pantallas dentro de MainActivity.
- Mantener LazyColumn para el directorio.
- Mantener itemId para navegar al expediente correspondiente.
- No agregar base de datos, API, Retrofit, Room, Hilt ni autenticación real.
- Utilizar datos estáticos.
- No eliminar funcionalidades existentes que ya funcionen.
- No modificar innecesariamente las versiones de Gradle o dependencias.
- Si necesitas agregar una dependencia, utiliza únicamente una que sea necesaria para el funcionamiento de la interfaz.

Al finalizar, comprueba que el proyecto compile y que el flujo completo funcione:

Login → Home → Directorio → Expediente
Login → Home → Perfil
Login → Recuperar contraseña
Home → Cerrar sesión → Login
Perfil → Cerrar sesión → Login