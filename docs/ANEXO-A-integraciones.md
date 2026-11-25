# ANEXO A: integraciones externas

| # | Integración | Tipo | Dónde (archivo:línea) | Propósito | Config o credencial (nombre) |
|---|---|---|---|---|---|
| 1 | App PinPad de Clip (`com.payclip.blaze.pinpad[suffix]`) | app destino por intent explícito | `ui/intent/system/SystemClipIntentProvider.kt:27,52` (`component = ClipApps.PINPAD_ENTRY`); `ui/launcher/resolver/ClipApps.kt:14` | cobrar en la terminal | sufijo de build `clipAppSuffix` (`pinpad-sdk/build.gradle.kts:10`) |
| 2 | App SmartPOS de Clip (`com.payclip.blaze.smartpos[suffix]`) | app destino por intent (allowlist) | `ui/launcher/resolver/ClipApps.kt:6-16` | cobrar en SmartPOS | ídem |
| 3 | Descubrimiento de apps por acción `com.payclip.action.CLIP_PAYMENT` | `PackageManager.queryIntentActivities` | `ui/launcher/resolver/ClipAppQuery.kt:13-24` | encontrar la app de Clip instalada | requiere `<queries>` en el manifiesto del host (ver comentario en `ClipAppQuery.kt`) |
| 4 | Server Side SDK (`api.payclip.io/f2f/pinpad/v1/payment`) | REST del backend Clip | README l.759-800 | crear cobros desde un backend | `Authorization: Basic <token>` (token del integrador, no está en el repo) |
| 5 | Webhook del integrador | HTTP entrante del integrador | README l.966-1017; `WEBHOOK_URL` en `SystemClipIntentProvider.kt:35,61` | recibir cambios de estado | URL del integrador |
| 6 | Retrofit, OkHttp, Gson | librerías de terceros | `gradle/libs.versions.toml` (`retrofit`, `okhttp`, `gson`) | cliente HTTP y serialización | ninguna |
| 7 | Compose BOM, activity-compose, lifecycle | AndroidX | `gradle/libs.versions.toml` | UI del selector de apps (`ClipAppChooser`) | ninguna |
| 8 | JitPack | registro Maven de GitHub | `jitpack.yml` (`jdk: openjdk18`), coordenadas `com.github.ClipMX` (`pinpad-sdk/build.gradle.kts:68-69`) | distribución del SDK a integradores | ninguna; se construye desde tags |
| 9 | GitHub Actions (CodeQL, dependency review) | CI | `.github/workflows/codeql.yml`, `dependecy-review.yml` | análisis y revisión de dependencias | `GITHUB_TOKEN` |
| 10 | Almacenamiento local (`SharedPreferences`) | local | `ui/launcher/resolver/ClipAppChoiceStore.kt:9-11` | recordar la app de Clip elegida | ninguna |
| 11 | Hosting de Firebase para settings | — | no aplica: `settings.gradle.kts` de este repo **no** aplica settings remotos | — | — |

## Detalle

### Apps de Clip destino

La resolución de la app (`ClipAppResolver.kt:14-27`) se hace así: primero se filtran los candidatos por `ALLOWED_PACKAGES`.
Si queda uno, se usa; si quedan varios, se usa el recordado o se muestra `ClipAppChooser`; si no queda ninguno, se usa el
componente fijo de PinPad (`fallback`).

### Backend Server Side

La API HTTP tiene dos llamadas (crear y consultar) y una de cancelación. El README no define SLA ni límites de tasa.
Los endpoints no están en el repo (no hay código de servidor).

### JitPack

`jitpack.yml` instala JDK `18.0.2-tem` con SDKMAN antes de compilar. JitPack compila desde el tag de Git que el integrador pide.
