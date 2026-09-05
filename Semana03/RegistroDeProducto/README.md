# RegistroDeProducto - Lab 03

**Desarrollado por:** Gonzalo

## Descripción
Esta aplicación permite registrar un producto ingresando su nombre, precio y cantidad. Al presionar el botón "AGREGAR PRODUCTO", se muestra un resumen con el importe total calculado y un mensaje de confirmación.

La aplicación utiliza **Jetpack Compose** y **Material 3**, aplicando jerarquía tipográfica y componentes como `OutlinedTextField`, `Button`, `Card` y `Scaffold`.

## Capturas de Pantalla

### 1. Pantalla Inicial (Vacía)
![Pantalla Inicial](app/src/main/res/drawable/screenshot_empty.png)

### 2. Producto Registrado
![Resumen](app/src/main/res/drawable/screenshot_filled.png)

## Pregunta: ¿Qué pasaría si declaras las variables de los campos SIN remember?

Si se declaran las variables sin `remember` (por ejemplo: `var nombre = ""`), el estado no persistirá durante las **recomposiciones**. 

En Jetpack Compose, cada vez que el valor de una variable de estado cambia (a través del `onValueChange`), la función composable se vuelve a ejecutar (recompone). Si no usamos `remember`, la variable se reiniciará a su valor inicial (`""`) en cada ejecución. Como resultado, **el usuario no podrá ver lo que escribe**, ya que el campo se limpiará instantáneamente al intentar actualizarse. `remember` permite que el valor se almacene en la memoria del árbol de composición y sobreviva a estos redibujados.
