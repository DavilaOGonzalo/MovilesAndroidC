# Base reconstruida de MuroCampus

La base se reconstruyó con asistencia de Codex a partir de la guía del Laboratorio 09 porque el proyecto original del docente no estaba disponible. Corresponde al paso 0. La actividad abre con la barra azul MuroCampus y el contenido vacío.

La guía muestra a `Usuario` como ejemplo entregado con Retrofit y serialización, y reserva esas dependencias y el permiso de Internet para el paso 1. En esta base están el modelo, el contrato de ejemplo, el repositorio con `Result` y los estados que compilan con Compose y ViewModel. La llamada real de `Usuario`, sus anotaciones y los mensajes específicos HTTP y de serialización quedan pendientes hasta que estén disponibles las dependencias de red. Al incorporarlas, el ViewModel debe recibir los errores a través del repositorio sin depender de Retrofit.

`Modelos.kt` y `RetrofitClient.kt` contienen los esqueletos guiados. La fábrica recibe una función de creación y podrá construir `PostsViewModel` cuando se implemente en su paso. El README con las capturas y las respuestas corresponde al paso 10.
