# PokemonFire

Aplicación Android para consultar Pokémon de tipo Fuego con Jetpack Compose y Material 3. El catálogo se obtiene de PokéAPI y se guarda en Room para mostrarlo sin conexión. Incluye detalle, favoritos locales, notificación al sincronizar, login Firebase y un panel visible para el rol `admin`.

## Estado

El código local está preparado en la rama `codigo-mio`. Antes de iniciar sesión por primera vez se debe completar la configuración manual de Firebase descrita más abajo. El archivo `google-services.json` es privado y está excluido de Git.

## Versiones

- Kotlin y plugins de Kotlin: 2.0.21
- Android Gradle Plugin: 8.7.2
- Gradle Wrapper: 8.9
- compileSdk / targetSdk: 35
- minSdk: 23
- Retrofit: 2.11.0
- Kotlinx Serialization: 1.7.3
- OkHttp: 4.12.0
- Room: 2.8.5
- Lifecycle: 2.8.7
- Navigation Compose: 2.8.4
- Firebase BoM: 33.5.1

Kotlin Serialization 1.7.3 se compila con el plugin de Kotlin 2.0.21; KSP usa la variante correspondiente a Kotlin 2.0.21. Room 2.8.5 requiere API 23 como mínimo, igual que `minSdk` del proyecto. El proyecto se compiló correctamente con Gradle 8.9 y Android Gradle Plugin 8.7.2.

## Arquitectura

```text
app/src/main/java/com/tecsup/pokemon/
├── AppContainer.kt
├── MainActivity.kt
├── data/
│   ├── auth/                 Firebase Authentication y Firestore
│   ├── local/                Room: Entity, DAO y Database
│   ├── mapper/               DTO/Entity/Domain
│   ├── remote/               Retrofit y DTO de PokéAPI
│   └── repository/           Implementación del repositorio Pokémon
├── domain/
│   ├── model/                Pokemon, Sesion y Rol
│   └── repository/           Interfaces de repositorio
├── notification/             Interfaz e implementación Android
└── ui/
    ├── admin/ detalle/ favoritos/ lista/ login/
    ├── components/ navigation/ theme/
    └── UiState.kt
```

Room es la fuente del catálogo que observa la UI. Retrofit actualiza Room y vuelve a aplicar el estado de favoritos durante la sincronización. Los ViewModels exponen `StateFlow<UiState<T>>` y no reciben `Context`.

## API

- Lista de tipo Fuego: `https://pokeapi.co/api/v2/type/fire`
- Detalle: `https://pokeapi.co/api/v2/pokemon/{nombre}`

## Configuración manual de Firebase

Completa estos pasos en Firebase Console con el proyecto que usarás para la app:

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Registra una aplicación Android con el ID exacto `com.tecsup.pokemon`.
3. Descarga `google-services.json` y colócalo dentro de la carpeta `app/`.
4. El plugin Google Services `4.4.2` ya está declarado en el `build.gradle.kts` raíz y aplicado en `app/build.gradle.kts`.
5. Sincroniza Gradle en Android Studio.
6. En Authentication → Sign-in method, activa Correo electrónico/contraseña.
7. En Authentication → Users, crea una cuenta para el administrador y otra para el usuario.
8. Crea la base de datos Cloud Firestore. En `usuarios`, crea un documento por cuenta, usando el UID de Authentication como ID del documento. Agrega `rol` (`admin` o `user`) y `nombre`.
9. Publica estas reglas de lectura de perfil:

```text
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

10. Ejecuta `gradlew.bat assembleDebug testDebugUnitTest` y abre la app en un emulador o dispositivo con Google Play Services.

El rol solo controla las pantallas que muestra la app. Las reglas de Firestore limitan la lectura del documento al UID autenticado. No agregues contraseñas ni `google-services.json` al repositorio.

## Compilar y probar

En Windows, con Android Studio/SDK configurado:

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
```

Las pruebas unitarias de `ListaViewModel` cubren la carga satisfactoria, el error sin caché y el cambio de favoritos.

## Interfaz y capturas

Las pantallas implementadas son Login, catálogo, detalle, Favoritos y Administración. Las capturas de la rúbrica se deben agregar después de instalar la app con Firebase configurado; no se incluyen capturas simuladas.

## Git

La rama de trabajo es `codigo-mio`. Los commits usan los prefijos `feat:`, `fix:`, `refactor:`, `test:`, `docs:` y `chore:`. No se configura remoto ni se hace push desde este proyecto.
