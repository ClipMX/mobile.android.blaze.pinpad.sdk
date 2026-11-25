# API pública: mobile.android.blaze.pinpad.sdk

Dos superficies: el **Terminal SDK** (Android, Kotlin, publicado en JitPack) y el **Server Side SDK** (HTTP, documentado en
`README.md` l.715-1092). Los símbolos `internal` no se listan.

## 1. Terminal SDK (Android)

Paquete raíz: `com.payclip.blaze.pinpad.sdk`.

### Entrada principal

| Símbolo | Archivo | Uso |
|---|---|---|
| `ClipPayment.Builder().build()` | `domain/builder/payment/ClipPayment.kt` | crea el cliente |
| `ClipPayment.setPaymentHandler(activity)` | `ClipPayment.kt` (y `ui/launcher/ClipLauncher.kt:21,37`) | registra el `PaymentListener` |
| `ClipPayment.start(reference, amount, ...)` | `ClipPayment.kt` (y `ui/launcher/ClipLauncher.kt:59,88`) | lanza el cobro en la app de Clip |
| `ClipPayment.Builder.setLoginCredentials(ClipPaymentLogin)` | `ClipPayment.kt:99` (README l.627-665) | sesión a usar en el cobro |
| `ClipPayment.Builder.setWebhookUrl(String?)` | `ClipPayment.kt:109` | URL de notificaciones del integrador |
| `ClipPayment.Builder.addListener(PaymentListener)` | `ClipPayment.kt:118` | listener de resultado |
| `ClipPayment.Builder.addLoginListener(LoginListener)` | `ClipPayment.kt:128`; `domain/listener/login/LoginListener.kt:3` | respuesta de login |
| `ClipPayment.Builder.isAutoReturnEnabled`, `isRetryEnabled`, `isShareEnabled`, `setPaymentPreferences` | `ClipPayment.kt:61,71,81,90` | preferencias del cobro |
| `ClipPayment.setPaymentHandler()` (sin activity) | `ClipPayment.kt:180` | variante sin `ComponentActivity` |

Nota: `ClipLauncher` es `internal` (`ui/launcher/ClipLauncher.kt:11`); el integrador usa `ClipPayment`.

### Listeners y resultados

| Tipo | Archivo | Métodos |
|---|---|---|
| `interface PaymentListener` | `domain/listener/payment/PaymentListener.kt:5` | `onSuccess(result: PaymentResult)`, `onCancelled()`, `onFailure(code: String)`, `onFailure(code, result: PaymentResult?)` (default, `-Xjvm-default=all`) |
| `interface LoginListener` | `domain/listener/login/LoginListener.kt:3` | respuesta de login |
| `data class PaymentResult` | `domain/models/payment/PaymentResult.kt:6` | datos de la transacción aprobada o fallida |
| `data class LoginResult` | `domain/models/login/LoginResult.kt:9` | resultado de login |
| `data class ClipPaymentLogin(userAccount, passwordAccount)` | `domain/models/login/ClipPaymentLogin.kt:9-12` | credenciales de cuenta (`@Keep`, `@Parcelize`) |
| `data class PaymentPreferences` | `domain/models/payment/settings/PaymentPreferences.kt:13` | preferencias recibidas de la app de Clip |
| `data class RequestPaymentPreferences` | `domain/models/payment/settings/RequestPaymentPreferences.kt:12` | preferencias que pide el integrador (propina, MSI, DCC, reintento, compartir, impresión) |

### Códigos de error

Los códigos de error del Terminal SDK están en el README (l.666-714). Llegan como `String` en `onFailure(code)`.

### Excepciones públicas

| Excepción | Archivo | Causa |
|---|---|---|
| `PaymentInitializationException` | `domain/models/exceptions/PaymentInitializationException.kt:3` | se llamó a `start` sin `setPaymentHandler` |
| `PaymentListenerInitializationException` | `.../PaymentListenerInitializationException.kt:3` | listener no inicializado |
| `LoginListenerInitializationException` | `.../LoginListenerInitializationException.kt:3` | login sin listener |
| `ApplicationNotFoundException` | `.../ApplicationNotFoundException.kt:3` | no hay PinPad ni SmartPOS de Clip instalados |

### Intents (contrato con la app de Clip)

`ui/intent/system/SystemClipIntentProvider.kt` construye el intent:

- `action = Intent.ACTION_MAIN`, `component = ClipApps.PINPAD_ENTRY` (en el caso de `getClipIntent` sin resolver) o el componente
  elegido por `ClipAppResolver`. Para el componente de PinPad: `com.payclip.blaze.pinpad[<sufijo>]/com.payclip.blaze.pinpad.shared.ui.MainActivity`.
- Acción de descubrimiento: `com.payclip.action.CLIP_PAYMENT`, categoría `DEFAULT` (`ClipAppQuery.kt`).
- Paquetes permitidos: `ClipApps.ALLOWED_PACKAGES = listOf(PINPAD_PACKAGE, SMARTPOS_PACKAGE)` (`ui/launcher/resolver/ClipApps.kt:16`).

Extras (nombre de clave, de `SystemClipIntentProvider.kt`): `PAYMENT_REFERENCE`, `PAYMENT_AMOUNT`, `PAYMENT_TIP_AMOUNT`,
`PAYMENT_AUTO_RETURN`, `PAYMENT_RETRY`, `PAYMENT_SHARE`, `PAYMENT_PREFERENCES`, `REQUEST_PAYMENT_PREFERENCES` (JSON),
`CLIP_LOGIN_CREDENTIALS` (Parcelable `ClipPaymentLogin`), `WEBHOOK_URL`, `SDK_VERSION`.

Ver SEGURIDAD: `CLIP_LOGIN_CREDENTIALS` lleva la contraseña en el intent.

### Resultado

`ui/activity/ClipResultManager.kt` (interfaz pública):

| Método | Línea | Uso |
|---|---|---|
| `setSuccessResult(...)` | `:20` | (uso interno de la app de Clip; el integrador no lo llama) |
| `setLoginSuccessResult`, `setLoginErrorResult`, `setErrorResult` | `:35`, `:48`, `:64` | ídem |
| `getResponse(result: ActivityResult)`, `getErrorCode(...)` | `:79`, `:88` | leer la respuesta del `ActivityResult` |
| `getLoginResponse`, `getLoginErrorResponse` | `:97`, `:106` | leer la respuesta de login |
| `parseResponse(...)`, `parseLoginResponse(...)` | `:116`, `:133`, `:145` | convertir a `PaymentResult` o `LoginResult` |

`ClipResultManagerFactory.create()` (`di/ui/activity/ClipResultManagerFactory.kt:8`) entrega la implementación.

### Otros públicos

| Símbolo | Archivo | Uso |
|---|---|---|
| `ClipIntentProvider` (interfaz) | `ui/intent/ClipIntentProvider.kt:8` | `getClipIntent(...)` (dos sobrecargas: con y sin propina), `getReference`, `getAmount`, `getTipAmount`, `getWebhookUrl`, `getSdkVersion`, `isAutoReturnEnabled`, `isRetryEnabled`, `isShareEnabled`, `getPaymentPreferences`, `getRequestPaymentPreferences`, `getPaymentLoginCredentials` |
| `ClipIntentProviderFactory.create()` | `di/ui/intent/ClipIntentProviderFactory.kt:8` | entrega la implementación |

## 2. Server Side SDK (HTTP)

Documentado en `README.md` l.715-1092. Base: `https://api.payclip.io` (el README lo marca como `<host>` en algunos ejemplos).

| Operación | Método y ruta | Auth | Referencia |
|---|---|---|---|
| Crear solicitud de cobro | `POST https://api.payclip.io/f2f/pinpad/v1/payment` | `Authorization: Basic <token>` (placeholder en el README) | README l.759-800 |
| Consultar cobro | `GET https://<host>/payment?pinpadRequestId=<id>` | ídem | README l.1025 |
| Cancelar cobro | `DELETE https://<host>/{pinpad_request_id}` | ídem | README l.904 |
| Notificación de cambio de estado (webhook del integrador) | `POST <webhook del integrador>` con `event_type: PINPAD_INTENT_STATUS_CHANGED` | — | README l.966-1017 |

Cuerpo de creación (ejemplo del README): `reference`, `amount`, `serial_number_pos`, `preferences` (`is_auto_return_enabled`,
`is_tip_enabled`, `is_msi_enabled`, `is_mci_enabled`, `is_dcc_enabled`, `is_retry_enabled`, `is_share_enabled`,
`is_auto_print_receipt_enabled`, `tip_options`, `redirect_package_name`).

Estados notificados por webhook (README l.979-989): `PENDING`, `IN_PROCESS`, `CANCELED`, `REJECTED`, `APPROVED`.
Polling opcional con la consulta `GET` (README l.1018-1052). Códigos de error del servidor en README l.1053-1083.

## Compatibilidad

El Terminal SDK se distribuye por JitPack con la versión del tag de Git. Los métodos marcados como `@Deprecated` en el
código o en el README deben seguir funcionando hasta la siguiente versión mayor.
