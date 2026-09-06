# Registro de Notas — Jetpack Compose

Este proyecto es una aplicación Android desarrollada para el ejercicio de la Semana 3, enfocada en la implementación de controles interactivos y lógica de cálculo utilizando Jetpack Compose.

## Objetivo
El objetivo de la aplicación es permitir a los estudiantes registrar sus notas de diferentes cursos del ciclo, calcular su promedio ponderado y obtener una observación académica basada en su rendimiento.

## Tecnologías Utilizadas
*   **Lenguaje:** Kotlin
*   **Framework:** Jetpack Compose (Material 3)

## Controles Utilizados
*   **Slider:** Para la asignación de notas de forma interactiva (0 a 20).
*   **Switch:** Para habilitar o deshabilitar el redondeo del promedio final.
*   **Checkbox:** Para confirmar la veracidad de los datos antes del cálculo.
*   **Button:** Para ejecutar la lógica de cálculo del promedio.

## Funcionamiento
### Asignación de Notas y Pesos
La aplicación permite asignar notas mediante Sliders para cuatro cursos con sus respectivos pesos:
1.  **Fundamentos de Programación:** 20%
2.  **Programación Orientada a Objetos:** 25%
3.  **Programación en Móviles:** 30%
4.  **Base de Datos:** 25%

### Cálculo del Promedio
El promedio ponderado se calcula aplicando la fórmula:
`Promedio = (N1 * 0.20) + (N2 * 0.25) + (N3 * 0.30) + (N4 * 0.25)`

### Observaciones
Se genera una observación automática basada en el promedio final:
*   **17 a 20:** EXCELENTE (Verde oscuro)
*   **13 a 16.99:** APROBADO (Verde)
*   **10 a 12.99:** EN RECUPERACIÓN (Ámbar)
*   **Menor a 10:** DESAPROBADO (Rojo)

## Capturas
![Registro de Notas](screenshot.png)

## Prompts utilizados durante el desarrollo

### Prompt 1 — Interfaz base:
Implementación inicial de la interfaz, los cuatro Sliders, Switch, Checkbox y botón.

> Modifica este proyecto de Jetpack Compose para implementar únicamente la interfaz inicial del ejercicio “Registro de Notas” de Semana 3. Sigue exactamente el diseño mostrado en la Figura 1 de la guía. Barra superior con el texto “Registro de Notas” y color primario morado. Fondo con degradado suave. Título “Notas del ciclo”. Texto “Desliza para asignar una nota (0 a 20)”. Crear exactamente 4 cursos: Fundamentos de Programación (20%), Programación Orientada a Objetos (25%), Programación en Móviles (30%) y Base de Datos (25%). Cada curso debe tener un Slider de 0 a 20. Los Sliders deben permitir únicamente valores enteros utilizando valueRange = 0f..20f y steps = 19. Mostrar al lado de cada Slider un badge con la nota seleccionada y actualizarlo en vivo. Cada Slider debe tener su propia variable remember con mutableStateOf. Agregar el Switch “Redondear promedio final”. Agregar el Checkbox “Confirmo que las notas son correctas”. Agregar el botón “CALCULAR PROMEDIO”. El botón debe iniciar deshabilitado y gris porque el Checkbox comienza desmarcado. Mostrar inicialmente el mensaje gris “Asigna las notas y confirma para calcular”. No implementes todavía el cálculo del promedio ni las observaciones. No agregues funcionalidades opcionales. Mantén el diseño adaptado a la pantalla de un celular y lo más parecido posible a la Figura 1 de la guía. No modifiques archivos innecesarios del proyecto.

### Prompt 2 — Funcionalidad:
Corrección del cálculo ponderado, promedio final, redondeo, observaciones y tarjeta de resultados.

> Continúa trabajando sobre la aplicación actual de Registro de Notas. En esta segunda etapa quiero una mejora funcional y visual, pero todavía no hagas el acabado final. No cambies la estructura general ni elimines los controles que ya funcionan. 1. CORREGIR EL CÁLCULO: Corrige el problema actual donde la tarjeta muestra 00.00 aunque se hayan seleccionado notas. El promedio ponderado debe calcularse exactamente con: Fundamentos de Programación → 20%, Programación Orientada a Objetos → 25%, Programación en Móviles → 30%, Base de Datos → 25%. Fórmula: notaFundamentos * 0.20 + notaPOO * 0.25 + notaMoviles * 0.30 + notaBD * 0.25. El promedio ponderado debe mostrarse con 2 decimales. 2. PROMEDIO FINAL: Si el Switch “Redondear promedio final” está apagado, mostrar el mismo promedio con 2 decimales. Si está encendido, utilizar roundToInt() y mostrar el resultado entero junto con: “(redondeado)”. 3. OBSERVACIÓN: Utiliza when sobre el promedio final: 17 a 20 → EXCELENTE, 13 a 16.99 → APROBADO, 10 a 12.99 → EN RECUPERACIÓN, menor a 10 → DESAPROBADO. El chip debe cambiar entre verde oscuro, verde, ámbar y rojo según corresponda. 4. TARJETA DE RESULTADO: Haz que la tarjeta se parezca más a la de la imagen de referencia: Promedio ponderado: 14.55, Promedio final: 15, mostrar (redondeado) cuando corresponda, chip APROBADO, mantener la tarjeta blanca con bordes redondeados. Después de calcular debe aparecer: “✓ Promedio calculado correctamente” en color verde. 5. MEJORAR LA APARIENCIA: Sin hacer todavía el acabado final, acerca la interfaz a la imagen de referencia: Mantén la barra superior morada. Mantén el fondo lavanda claro. Haz que el título “Notas del ciclo” y los nombres de cursos tengan una apariencia más parecida a la referencia. Ajusta ligeramente tamaños, márgenes y espacios para que la pantalla sea más compacta. Mantén los Sliders morados. Mantén los badges morados con números blancos. Mantén el botón morado cuando esté habilitado y gris cuando esté deshabilitado. 6. IMPORTANTE: No agregues todavía funcionalidades opcionales. No agregues botón LIMPIAR, slider con semáforo, ni aporte individual por curso. Esta es solamente la segunda etapa. Corrige principalmente el cálculo, el resultado y realiza un primer ajuste visual. El acabado visual completamente idéntico a la referencia se realizará en una tercera etapa.

### Prompt 3 — Diseño final:
Mejoras visuales, distribución de los resultados, presentación de los dos promedios y ajustes finales de la interfaz.

> Realiza la tercera y última etapa de la aplicación. No cambies ninguna lógica ni cálculo que ya funciona correctamente. No agregues funcionalidades nuevas. Enfócate únicamente en que la interfaz final sea lo más parecida posible al prototipo de la guía, especialmente a las Figuras 1 y 2. Ajusta: La barra superior morada y su altura para que se parezca al prototipo. El título “Registro de Notas” y su posición. El título “Notas del ciclo”, su tamaño y espaciado. El fondo lavanda claro con degradado suave. Los márgenes laterales y el espacio vertical entre los cuatro cursos. Los nombres de los cursos y los pesos para que tengan tamaños similares a la referencia. Los Sliders para que tengan una longitud, grosor y posición similares al prototipo. Los badges de las notas para que tengan el tamaño y forma redondeada de la referencia. El Switch y Checkbox para que tengan una posición y tamaño similares. El botón “CALCULAR PROMEDIO” para que tenga bordes redondeados, tamaño y posición similares a la Figura 2. La tarjeta de resultados para que tenga la misma estructura visual de la referencia: Promedio ponderado: 14.55, Promedio final: 15, (redondeado) debajo del promedio final, Chip APROBADO debajo. El mensaje “✓ Promedio calculado correctamente” debe estar fuera de la tarjeta, debajo de ella, como aparece en la Figura 2. Agrega/ajusta el pie inferior: “Desarrollado por: Gonzalo Davila Ochochoque” en tamaño pequeño y color gris. Ajusta el contenido para que todo entre correctamente en la pantalla del celular sin quedar cortado. Compara visualmente el resultado con las Figuras 1 y 2 de la guía y realiza los ajustes necesarios de tamaños, padding, spacing, alineación y posiciones. Importante: conserva exactamente la lógica actual de los Sliders, Switch, Checkbox, botón, promedio ponderado, roundToInt(), observaciones y colores de los chips. No agregues botón LIMPIAR, semáforo ni aporte por curso.

---
**Desarrollado por:** Gonzalo Davila Ochochoque
