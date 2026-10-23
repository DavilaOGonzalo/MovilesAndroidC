# LavaCampus

Aplicación móvil para gestionar una lavandería universitaria con dos perfiles en el mismo dispositivo:
**Estudiante** y **Administrador**. El estudiante crea pedidos con varias líneas de servicio y sigue su
estado; el administrador controla pedidos, casilleros, cobros, tarifas, clientes e indicadores.

## Tecnología y arquitectura

- Kotlin, Jetpack Compose y Material 3, sin XML.
- Room con SQLite (`lavacampus.db`), DataStore Preferences y Navigation Compose.
- MVVM + Repository mediante `LavaRepository` como contrato.
- Datos ficticios locales para la práctica; no se usan datos personales reales.
- `minSdk 26` para trabajar con `java.time`.

```text
Composable (ui/) ── eventos ──▶ ViewModel ──▶ LavaRepository (interface)
       ▲                         │                  │
       └──── StateFlow / Flow ───┘                  ▼
                                      LavaRepositoryRoom ──▶ DAO ──▶ Room/SQLite

DataStore ──▶ SesionViewModel / preferencias de la interfaz
```

Las reglas de negocio viven en los ViewModels. Las pantallas no conocen DAOs ni implementaciones del
Repository; el Repository no conoce Composables.

## Estructura de paquetes

```text
com.tecsup.lavacampus
├── MainActivity.kt
├── LavaApp.kt
├── data
│   ├── local
│   │   ├── entity       Servicio, Usuario, Pedido, DetallePedido
│   │   ├── dao          ServicioDao, UsuarioDao, PedidoDao, DetalleDao
│   │   ├── relation     DetalleConServicio, PedidoConDetalle
│   │   ├── LavaDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── prefs             PreferenciasStore.kt
│   └── repository        LavaRepository, LavaRepositoryRoom
├── model                 Estados, roles, líneas, gráficos y resúmenes
├── viewmodel             StateFlow y reglas de estudiante/administrador
└── ui
    ├── components        Componentes Material 3 y Canvas reutilizables
    ├── navigation         Rutas y NavHost
    ├── theme              Tema turquesa/violeta y modo oscuro
    ├── estudiante         Login, inicio, pedido, detalle, historial, tarifas, perfil
    └── administrador      Panel, bandeja, gestión, servicios, clientes y ajustes
```

## Reglas implementadas

- Un estudiante solo observa sus pedidos y puede tener hasta tres activos.
- Cantidades KG de 1 a 10 en pasos de 0.5; prendas/unidades enteras de 1 a 30.
- Los precios quedan congelados en cada detalle y los pedidos se guardan en una transacción.
- La fecha estimada suma el mayor tiempo de servicio y salta domingos.
- La máquina de estados solo avanza: Recibido → En proceso → Listo → Entregado.
- Listo requiere casillero libre `C-01` a `C-30`; Entregado requiere pago confirmado.
- Cancelar está limitado a Recibido para estudiantes y a Recibido/En proceso para administradores.
- Servicios usados no se eliminan; solo se pueden desactivar.
- El modo oscuro, sesión, avisos y recepción de pedidos se conservan con DataStore.

## Mejoras de la rama `codigo-ia`

1. **Pedidos atrasados:** consulta Room, filtro en la bandeja, marca roja y contador en el panel.
2. **Descuento por volumen:** más de 5 kg aplica 10 %, se muestra en el resumen y se almacena en `Pedido`.
3. **Repetir pedido:** desde el historial carga las mismas líneas, aplicando los precios vigentes.

La migración Room `1 → 2` agrega la columna `descuento` sin perder pedidos existentes. El proyecto usa
Room `2.7.2` porque es el procesador compatible con KSP2 del toolchain instalado; la API y la estructura
corresponden al contrato Room del laboratorio.

## Credenciales de práctica

| Perfil | Correo | Clave |
|---|---|---|
| Estudiante | `ana@correo.test` | `1234` |
| Estudiante | `luis@correo.test` | `1234` |
| Estudiante | `marta@correo.test` | `1234` |
| Administrador | `admin@lavacampus.test` | `admin` |

## Capturas

### Estudiante

![Inicio del estudiante](docs/capturas/estudiante-inicio.png)
![Nuevo pedido](docs/capturas/estudiante-nuevo-pedido.png)
![Mis pedidos](docs/capturas/estudiante-mis-pedidos.png)

### Administrador

![Panel del administrador](docs/capturas/admin-panel.png)
![Bandeja de pedidos](docs/capturas/admin-bandeja.png)
![Gestión de pedido](docs/capturas/admin-gestion.png)

## Pruebas de aceptación

- [x] Login válido e inválido con mensaje de error.
- [x] Confirmar pedido deshabilitado sin servicios.
- [x] Cálculo de total, fecha, salto de domingo y límite de tres activos.
- [x] Cancelación condicionada por estado.
- [x] Casillero ocupado/desconocido rechazado al marcar Listo.
- [x] Entrega rechazada sin cobro y aceptada con cobro.
- [x] Cambios del administrador se reflejan por `Flow` en el estudiante.
- [x] Ingresos, clientes, servicios y preferencias persistentes.
- [x] Descuento por volumen, pedidos atrasados y repetir pedido.

## Para pensar

**¿Por qué los detalles están en otra tabla?** Un pedido puede tener muchos servicios. La relación
uno-a-muchos evita columnas repetidas y permite calcular ingresos por servicio con `JOIN`, `SUM` y
`GROUP BY`.

**¿Por qué se guarda `precioUnitario` y `subtotal`?** Son una fotografía del precio al momento de
comprar. Si la tarifa cambia después, el historial y los ingresos de pedidos antiguos no se alteran.

**¿Por qué unas operaciones usan `Flow` y otras `suspend`?** Una lectura que debe actualizar la UI se
observa con `Flow`; una escritura o consulta única se ejecuta como `suspend`. Son responsabilidades
complementarias.

**¿Por qué crear el pedido y sus detalles en una transacción?** Para evitar pedidos incompletos: si falla
una línea, Room revierte también el pedido principal.

**¿Por qué las reglas están en el ViewModel?** El ViewModel coordina estado y decisiones de negocio sin
acoplarlas a una pantalla. Así la misma regla sirve para distintas vistas y el Repository puede cambiar
de Room a un servicio web en otra semana.

**¿Por qué DataStore no guarda pedidos?** DataStore es un almacén clave-valor para preferencias pequeñas;
las relaciones, consultas y transacciones de pedidos pertenecen a Room.

## Compilación

Desde `Semana08`:

```powershell
./gradlew.bat :app:assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.
