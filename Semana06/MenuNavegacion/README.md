# MenuListasPerfil | Portal del Alumno

Este es un proyecto educativo de Android desarrollado con **Kotlin**, **Jetpack Compose** y **Navigation Compose**. El proyecto implementa una arquitectura limpia y desacoplada mediante la separación de la lógica de navegación y los componentes de la interfaz de usuario en módulos y archivos independientes (`navigation` y `screens`).

La aplicación sirve como ejemplo práctico para la gestión estructurada de rutas de navegación, la transferencia de argumentos fuertemente tipados a través del `NavHost` y la correcta manipulación del historial del contenedor de pantallas (`back stack`).

---

## Requerimientos funcionales

A continuación, se detallan los requerimientos funcionales implementados en la aplicación, respaldados estrictamente por el código fuente actual del proyecto:

### 1. Inicialización desde el punto de entrada principal
La aplicación inicia de forma exclusiva desde `MainActivity`, la cual establece el tema de diseño general `MenuListasPerfilTheme` y ejecuta la función composable `AppNavigation()` dentro de su método contenedor `setContent`. Esto delega el control de la interfaz por completo al grafo de navegación central.

### 2. Flujo de navegación desde el menú principal
La pantalla de inicio (`HomeScreen`) ofrece una estructura clara con controles interactivos que permiten al usuario redirigirse de manera directa hacia dos flujos independientes de la aplicación: la pantalla con la lista indexada de elementos (`ListScreen`) y la pantalla con la ficha técnica de perfil de usuario (`ProfileScreen`).

### 3. Visualización y selección en lista optimizada
La pantalla de elementos (`ListScreen`) genera de manera dinámica una colección de 8 componentes empleando un contenedor eficiente `LazyColumn`. Cada fila o ítem de la lista (`ListItem`) es completamente cliqueable y permite al usuario seleccionar un elemento específico para acceder de inmediato a su respectiva vista pormenorizada.

### 4. Recepción de argumentos tipados en detalle
La pantalla de especificaciones (`DetailScreen`) está configurada en el grafo de navegación para recibir de manera obligatoria y fuertemente tipada un identificador entero (`itemId`) mediante un argumento incrustado en la URI de la ruta (`"detail/{itemId}"`). El componente extrae este valor numérico del `NavBackStackEntry` para renderizar el identificador correspondiente del elemento seleccionado.

### 5. Control del historial de navegación inverso
Las vistas de lista (`ListScreen`), detalle (`DetailScreen`) y perfil (`ProfileScreen`) cuentan con botones dedicados en la interfaz de usuario que invocan la función `navController.popBackStack()`. Esto garantiza que el usuario pueda deshacer su acción y regresar con seguridad a la pantalla inmediatamente anterior sin romper el flujo de la aplicación.

### 6. Gestión de perfil y limpieza del Back Stack
La pantalla de perfil (`ProfileScreen`) despliega los datos informativos del usuario y provee un botón para regresar al inicio. Dicha acción ejecuta una navegación hacia la ruta raíz (`Home`), configurada con una regla `popUpTo(Screen.Home.route) { inclusive = true }` que limpia por completo el historial de pantallas acumuladas para prevenir la redundancia o duplicación de instancias de la pantalla principal en la pila de memoria.
