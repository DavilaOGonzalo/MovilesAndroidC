# Prompts utilizados

## Prompt 1 — pedidos atrasados

**Texto enviado:**

> En una app Kotlin con Compose, Room y MVVM + Repository, agrega una consulta que devuelva los IDs de
> pedidos cuya fecha de entrega ya pasó y cuyo estado no sea ENTREGADO ni CANCELADO. Declara primero la
> función en `LavaRepository`, impleméntala en `LavaRepositoryRoom`, expón el resultado como `StateFlow`
> en la bandeja y muestra una marca roja en la View. No uses DAO ni Repository desde la View.

**Resultado aplicado:** consulta `PedidoDao.observarAtrasados`, estado reactivo en panel/bandeja, filtro
`Atrasados` y marca visual.

**Correcciones realizadas:** se mantuvo la consulta detrás de la interfaz `LavaRepository` y se agregó
el contador del panel sin mover reglas a los Composables.

**¿Respeta MVVM?:** Sí.

## Prompt 2 — descuento por volumen

**Texto enviado:**

> Agrega un descuento del 10 % cuando el pedido supera 5 kg. El cálculo debe vivir en la capa de
> negocio/Repository, mostrarse en el resumen y conservarse en Room. Incrementa la versión y crea una
> migración; no rompas la transacción pedido-detalles.

**Resultado aplicado:** columna `Pedido.descuento`, migración Room 1→2, cálculo transaccional, resumen y
detalle del pedido.

**Correcciones realizadas:** el total se calcula sobre el subtotal y los detalles mantienen su subtotal
original para conservar el precio congelado por línea.

**¿Respeta MVVM?:** Sí; la View solo renderiza StateFlow.

## Prompt 3 — repetir un pedido

**Texto enviado:**

> Desde el historial, agrega “Pedir de nuevo”. Carga las cantidades de las líneas en Nuevo pedido, pero
> usa los precios actuales al confirmar. Mantén la navegación Compose y deja la lectura en el ViewModel.

**Resultado aplicado:** navegación con `repeatId`, carga reactiva del pedido y creación de un nuevo pedido
mediante el Repository, que vuelve a consultar las tarifas vigentes.

**Correcciones realizadas:** se filtran servicios inactivos y se informa al usuario que los precios pueden
haber cambiado.

**¿Respeta MVVM?:** Sí.

## Reflexión

- La asistencia ayudó a revisar consultas SQL, migraciones y combinaciones de `Flow`.
- No se aceptó código que accediera al DAO desde una pantalla.
- El error más importante fue asumir que una mejora de datos no necesitaba migración.
- Room exige una transacción para que pedido y detalles sean una sola operación consistente.
- MVVM permite cambiar la fuente local por una API manteniendo las pantallas y reglas aisladas.
