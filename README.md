# mobile.android.blaze.pinpad.sdk

SDK público de Clip para cobrar con una terminal Clip (PinPad o SmartPOS) desde una app de terceros. Tiene dos
caras: el **Terminal SDK** (Android, publicado en JitPack) que lanza el cobro en la app de PinPad o SmartPOS de Clip
por intent, y el **Server Side SDK** (REST, documentado en este README) que crea la solicitud de cobro desde un backend
y recibe el estado por webhook o polling.

Este repo contiene sólo el código del Terminal SDK (módulo `:pinpad-sdk`, namespace `com.payclip.blaze.pinpad.sdk`).
El Server Side SDK es una API HTTP en `api.payclip.io` y no tiene código en este repo; se documenta en el README (l.715-1092).

El README propio es documentación pública para integradores (1103 líneas): propósito, requisitos, dispositivos soportados,
dependencia, ejemplos de código, manejo de sesión, códigos de error y la API del servidor. No describe arquitectura
interna, CI/CD ni ejecución local del proyecto.

Documentación complementaria en esta carpeta:

- [API.md](API.md): clases públicas del Terminal SDK, intents y contrato de resultado; endpoints del Server Side SDK.
- [ANEXO-A-integraciones.md](ANEXO-A-integraciones.md): apps de Clip destino, backend, JitPack y dependencias.
- [ANEXO-B-infraestructura.md](ANEXO-B-infraestructura.md): build, publicación en JitPack, CI y CodeQL.
- [architecture.png](architecture.png) (fuente: [architecture.puml](architecture.puml)): cómo se conecta el integrador con la app de Clip.
- [SEGURIDAD.md](SEGURIDAD.md): hallazgos de la revisión de lectura.

## Stack

| Componente | Detalle (fuente) |
|---|---|
| Lenguaje | Kotlin (`kotlin = 2.4.10` en `gradle/libs.versions.toml`), JVM target 11 |
| UI | Jetpack Compose (BOM `2024.02.02`), activity-compose, lifecycle |
| Red | Retrofit `2.9.0`, OkHttp `4.11.0` y logging-interceptor, Gson `2.10.1` (declarados en el catálogo; ver SEGURIDAD) |
| Android | `minSdk = 22`, `compileSdk = 34` (`pinpad-sdk/build.gradle.kts:17-22`) |
| Build | AGP `9.3.0` (catálogo), Gradle wrapper |
| Publicación | JitPack (`jitpack.yml`, JDK `openjdk18`), `maven-publish` con `groupId = com.github.ClipMX` |
| Tests | JUnit 4, MockK, Mockito-Kotlin, coroutines-test |

No es KMM. No hay Python ni Node. No hay Terraform ni AWS en el repo.

## Estructura

```
settings.gradle.kts                    incluye :pinpad-sdk (sin settings remotos)
pinpad-sdk/src/main/java/com/payclip/blaze/pinpad/sdk/
  domain/builder/payment/ClipPayment   builder público (ClipPayment.Builder().build())
  domain/listener/                     PaymentListener, LoginListener
  domain/models/                       PaymentResult, LoginResult, ClipPaymentLogin, PaymentPreferences, RequestPaymentPreferences
  domain/models/exceptions/            PaymentInitializationException, ApplicationNotFoundException, etc.
  ui/intent/                           ClipIntentProvider (interfaz pública) y SystemClipIntentProvider (construye el intent)
  ui/activity/                         ClipResultManager (público) y ActivityClipResultManager
  ui/launcher/                         ClipLauncher (interno) y ActivityClipLauncher; resolver/ (ClipApps, ClipAppQuery, ClipAppResolver, ClipAppChooser)
  di/                                  factories públicas (ClipIntentProviderFactory, ClipResultManagerFactory, ClipLauncherFactory)
pinpad-sdk/src/test/                   pruebas (ClipPaymentTest, etc.)
```

## Flujo principal (Terminal SDK)

1. El integrador crea el cliente: `val client = ClipPayment.Builder().build()`.
2. Registra un `PaymentListener` (`Builder.addListener`) y, opcionalmente, un `LoginListener` (`Builder.addLoginListener`). Luego llama `client.setPaymentHandler(activity)`.
3. Llama `client.start(reference, amount, ...)`. El SDK arma un intent `ACTION_MAIN` con `component` explícito de la app
   de Clip (PinPad o SmartPOS). Los extras incluyen referencia, monto, preferencias de cobro, credenciales opcionales y webhook opcional.
4. `ClipAppQuery` busca las apps de Clip que declaran `com.payclip.action.CLIP_PAYMENT`. `ClipAppResolver` las filtra por
   `ALLOWED_PACKAGES` (PinPad y SmartPOS con el sufijo del build). Si hay una, se usa; si hay varias, se pregunta al usuario
   (`ClipAppChooser`) y se recuerda la elección (`ClipAppChoiceStore`). Si no hay ninguna, se usa la actividad de PinPad.
5. La app de Clip cobra en la terminal. El resultado vuelve por `ActivityResult` y `ClipResultManager.parseResponse` lo
   convierte en `PaymentResult` (éxito, cancelado o error con código). Ver `PaymentListener` (`onSuccess`, `onCancelled`, `onFailure(code)`).

## Configuración

El integrador configura:

| Nombre | Dónde | Uso |
|---|---|---|
| `ClipPayment.Builder()` | `domain/builder/payment/ClipPayment.kt` | cliente del SDK |
| `setLoginCredentials` (usuario y contraseña de la cuenta Clip) | `domain/models/login/ClipPaymentLogin.kt` | sesión a usar en el cobro (ver README, l.627-665) |
| `webhookUrl` | parámetro de `start` | URL que recibe los cambios de estado (ver README, §WebHook) |
| `clipAppSuffix` (propiedad Gradle del propio SDK, no del integrador) | `pinpad-sdk/build.gradle.kts:10` | sufijo de paquete para builds QA (ver README del repo, l.26-37 de la app PinPad) |

Ningún valor de cuenta ni de API key está en el repo.

## Ejecución local y pruebas

Dependencia para integradores (README §"Installing dependency", l.193-222), vía JitPack:

```kotlin
implementation("com.github.ClipMX:mobile.android.blaze.pinpad.sdk:<versión>")
```

Para el equipo que mantiene el SDK:

```sh
./gradlew :pinpad-sdk:testDebugUnitTest
./gradlew :pinpad-sdk:publishToMavenLocal -PclipAppSuffix=.qa   # publica <versión>-qa en mavenLocal (uso interno, no se publica)
```

El `publishToMavenLocal` con `clipAppSuffix` genera un SDK que apunta a las apps `.qa`. El README indica no subir ese artefacto.

## Publicación

Las versiones salen de tags de Git (JitPack construye desde el tag). `sdkVersion` se toma de la variable de entorno
`GITHUB_REF_NAME` o `VERSION` (`pinpad-sdk/build.gradle.kts:13`). Ver ANEXO-B.
