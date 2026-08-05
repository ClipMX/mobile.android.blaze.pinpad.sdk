# Contexto del Proyecto — PinPad SDK

> **Propósito de este archivo:** Bitácora viva del trabajo en este proyecto. Sirve como contexto para cualquier sesión de Claude Code: si la sesión se cierra, leer este archivo permite recuperar el contexto rápidamente. **Mantenerlo actualizado al final de cada sesión de trabajo o después de cada cambio/decisión relevante.**

---

## 🎯 Finalidad del proyecto

- **Repo:** `ClipMX/mobile.android.blaze.pinpad.sdk`
- SDK de Android (PinPad SDK) de Clip para integrar procesamiento de pagos en apps de terceros.
- Ofrece dos soluciones:
  - **Terminal SDK (app-to-app):** integración nativa Android para lanzar cobros en terminales Clip.
  - **Server Side SDK:** API server-driven para crear/eliminar solicitudes de pago y recibir resultados vía webhook.
- Distribución vía JitPack (ver `jitpack.yml`). Módulo principal: `pinpad-sdk/`.
- Versión actual documentada: **1.0.9.1**.

### Objetivo de la sesión / iniciativa actual

**Validación de identidad en pagos app-to-app (SDK → Pinpad).** Ticket padre: [F2F-834](https://linear.app/payclip/issue/F2F-834) (subtareas F2F-835…F2F-844).

#### Problema

- Pinpad procesa los pagos app-to-app con la **sesión/cuenta logueada en el dispositivo**, sin validar que corresponda al comercio dueño de la integración.
- **Vector de fraude:** un cajero tranza cierra la sesión del comercio en pinpad y se loguea con su propia cuenta → los pagos del SDK se depositan a la cuenta del cajero.
- El BE hoy no distingue un cobro normal de uno vía SDK: el fraude no es visible ni medible.

#### Solución

1. **SDK**: crea la intención de pago (`POST f2f/pinpad/v1/mobile/payment`) autenticada con API key/secret del comercio (`Authorization: Basic`) **antes** de lanzar el intent, y agrega el extra `PINPAD_REQUEST_ID`.
2. **Pinpad**: lee el extra, consulta el status del request al BE con las credenciales de la sesión activa y gatea el cobro — **2xx** continúa, **40x** bloquea. Sin request (SDK viejo) → flujo actual (fase opcional).
3. **BE** (iniciativa aparte): matching de cuentas/subcuentas y, a futuro, enforcement server-side.

#### Decisiones de diseño (2026-08-04)

- Inicialización global `ClipPaymentSDK.initialize(apiToken, environment)`; enum `ClipEnvironment` (PRODUCTION/STAGE). El `apiToken` es el token del portal de developers ya armado (`Basic base64(apiKey:secretKey)`, con o sin prefijo) — se decidió pedirlo como credencial única en vez de apiKey/secretKey por separado porque el portal lo entrega así (decisión 2026-08-04, mejor DX y menos error de integración).
- `serial_number_pos`: aleatorio `sdk-<uuid>`, estable por proceso (identificable en BE).
- **Fail-open en fase de transición**: si la creación del request falla, el intent se lanza sin `request_id`; resultado vía `PaymentRequestListener` (`onPaymentRequestCreated`/`onPaymentRequestFailed`).
- Sin librerías internas de Clip en el SDK (solo Retrofit/OkHttp/Gson, ya estaban en el classpath).
- Sin reintentos en la creación (sin idempotency key un retry puede duplicar órdenes — pinpad hoy reintenta x5 a ciegas, NO replicado). Dinero como `BigDecimal`, nunca Float.
- Timeout de creación: 5 s (`withTimeoutOrNull` + timeouts de OkHttp) para no retrasar el cobro.
- `start()` conserva firma síncrona (no breaking change); orquesta con scope interno `Dispatchers.Main.immediate`.

#### Riesgos conocidos / plan de cierre

- **Mientras el request sea opcional el hueco sigue abierto** (downgrade de APK lo esquiva). Plan: (1) BE etiqueta transacciones app-to-app para medir adopción y mismatches, (2) enforcement server-side con flag por comercio → global, (3) versión mínima de pinpad forzada desde BE.
- La barrera real debe vivir en el charge del BE (el status check de pinpad es fail-fast de UX, bypasseable por clientes viejos).

#### Dudas abiertas

- [ ] ¿La validación de coincidencia de cuentas ocurrirá también **al procesar el cobro** (charge con `request_id`) o solo en el status check? (crítica)
- [ ] ¿Cómo valida el BE las subcuentas? ¿Qué relación cuenta-subcuenta es válida?
- [ ] ¿Fecha/plan para volver obligatorio el request (fin de fase opcional)?
- [ ] Pinpad ante error de red en la validación: ¿fail-open (propuesto) o bloquear?
- [ ] TTL y un-solo-uso del request en BE (evitar replays).
- [ ] El `GET f2f/pinpad/v1/payment` existe en BE pero pinpad no lo consume aún; confirmar semántica 403/404/410 para UX/analytics.

---

## 🔨 Cambios realizados

| Fecha | Cambio | Archivos/Módulos | PR/Ticket |
|-------|--------|------------------|-----------|
| 2026-07-30 | Se creó este archivo de contexto y `CLAUDE.md` que lo referencia | `PROJECT_CONTEXT.md`, `CLAUDE.md` | — |
| 2026-08-04 | Creación de payment request desde el SDK: `ClipPaymentSDK.initialize`, `ClipEnvironment`, capa `data/` completa (AuthInterceptor Basic, `PaymentRequestApi`, DTOs BigDecimal, repository sin retries, `NetworkFactory`), `CreatePaymentRequestUseCase`, `PaymentRequestListener`, extra `PINPAD_REQUEST_ID` en el intent, orquestación fail-open en `ClipPayment.start()`. Compila (`assembleRelease` OK). Sin commit aún | `pinpad-sdk/` (14 archivos nuevos, 7 modificados) | F2F-834…841 |
| 2026-08-04 | **Fix bug preexistente**: el overload de `ActivityClipLauncher.startPayment` con `tipAmount` no lo pasaba a `getClipIntent` → el extra `PAYMENT_TIP_AMOUNT` nunca se enviaba (feature de propina rota desde PR #44) | `ActivityClipLauncher.kt` | F2F-838 |
| 2026-08-04 | README: sección de inicialización real (`ClipPaymentSDK.initialize`) corrigiendo el drift (documentaba user/API key en el builder que nunca existieron) + doc de `addPaymentRequestListener`. `consumer-rules.pro` con keeps para DTOs Gson | `README.md`, `consumer-rules.pro` | F2F-839/840 |
| 2026-08-04 | **Pinpad**: validación de ownership implementada — lee `PINPAD_REQUEST_ID` del intent (constante espejo, hasta hacer bump del SDK), `GET f2f/pinpad/v1/payment?pinpadRequestId=` (header `Pinpad-Include-Detail: false`, sin retries, `ApiHttpException` preservada), `ValidateOrderOwnershipUseCase` → `OwnershipValidationResult` (Valid/Rejected 4xx/Unverifiable), gateo en `SDKChargeViewModel.createOrder`: **4xx bloquea** con `Declined("PAYMENT_REQUEST_OWNERSHIP_REJECTED")` de vuelta al integrador; sin request o inverificable (red/5xx) → continúa (fail-open). Compila (`compileReleaseKotlin` OK). Sin commit | `../mobile.android.blaze.pinpad` (11 modificados, 3 nuevos) | F2F-842/843/844 |

### Historial reciente previo (referencia, ya en git)

- `a74c68c` — [MMSDK-836] Soporte para Java 8 (#45)
- `7453c35` — Nuevo parámetro para propina (tip) (#44)
- `81b494d` — [MMSDK-612] Envío de payment preferences en formato JSON (#43)
- `833df54` — [HK-3770] Split Payments

---

## 🧭 Decisiones

| Fecha | Decisión | Razón / Contexto |
|-------|----------|------------------|
| 2026-07-30 | Usar `PROJECT_CONTEXT.md` como bitácora de contexto entre sesiones, referenciado desde `CLAUDE.md` | Recuperar contexto fácilmente si la sesión de Claude se cierra |
| 2026-08-04 | Este archivo es el **doc único** de la iniciativa de validación app-to-app (se eliminó `VALIDACION_PAGOS_APP_TO_APP.md` del repo pinpad) | Un solo lugar para contexto, avances, pruebas y dudas |
| 2026-08-04 | Request **opcional** en fase de transición (fail-open); migraciones de SDK y pinpad se preparan a nivel operativo | No romper integraciones existentes mientras se gana adopción |
| 2026-08-04 | En ruta asíncrona, `ApplicationNotFoundException` se entrega como `PaymentListener.onFailure("APPLICATION_NOT_FOUND")` en lugar de throw | Un throw dentro de corrutina crashearía la app del integrador sin posibilidad de try/catch |
| 2026-08-05 | El package de pinpad que abre el intent se deriva del `ClipEnvironment`: `PRODUCTION` → release (sin override posible), `STAGE` → `.qa` por default u override opcional `debugPinpadPackage` (p. ej. `.dev`) | El SDK es un AAR único (no hereda buildType del integrador); amarrarlo al ambiente elimina el hack local de pruebas y evita apuntar a builds de debug en producción |

---

## 📋 Pendientes

### Sesión 2026-08-05 (agenda acordada)

- [ ] **Error codes hacia el integrador**: revisar/definir el catálogo completo que recibe el usuario del SDK — los de creación del request (`SDK_NOT_INITIALIZED`, `PAYMENT_REQUEST_UNAUTHORIZED`, `PAYMENT_REQUEST_INVALID`, `PAYMENT_REQUEST_SERVER_ERROR`, `PAYMENT_REQUEST_NETWORK_ERROR`), el de bloqueo (`PAYMENT_REQUEST_OWNERSHIP_REJECTED`), `APPLICATION_NOT_FOUND` (nuevo, ruta asíncrona) y los preexistentes; validar nomenclatura, semántica y documentarlos en el README.
- [ ] **Status de la transacción**: revisar qué estados se reportan de la txn al BE y al integrador en cada camino (aprobada, declinada, cancelada, bloqueada por ownership) — hoy el bloqueo NO actualiza el status del request en BE (¿debería marcarse CANCELED/REJECTED para que no quede huérfano en PENDING/IN_PROCESS?).
- [ ] **Eventos de analytics de errores**: auditar los eventos que se disparan en fallos (creación del request, validación de ownership, mismatch — la señal de fraude) y definir los que falten; hoy el rechazo se trackea con `terminalSideErrorEvent("PAYMENT_REQUEST_OWNERSHIP_REJECTED_<code>")`.
- [ ] **Pruebas**: completar la matriz pendiente (subcuentas válidas, request expirado/ya usado, credenciales inválidas 401, modo avión, regresión sin request/SDK viejo, downgrade) y arrancar los tests unitarios de lo implementado.

### Generales

- [ ] **Pinpad (F2F-842/843/844)**: leer `PINPAD_REQUEST_ID` del intent, `GET f2f/pinpad/v1/payment?pinpadRequestId=` con credenciales de sesión, gatear el cobro (40x bloquea).
- [ ] **Verificación manual contra stage (F2F-841)**: requiere API key/secret de prueba del portal de developers.
- [ ] **Actualizar/crear documentación** conforme avance: este doc (avances/pruebas/decisiones), README del SDK si cambia la API pública, y docs de pinpad cuando se implemente su parte.
- [ ] Tests unitarios (después de que todo funcione end-to-end — decisión de la iniciativa).
- [ ] Commits/PR (gateados a la tarea de Linear F2F-834, ya creada; falta confirmación para commitear).
- [ ] Resolver dudas abiertas con BE (ver sección de la iniciativa arriba).

## 🧪 Pruebas

| Fecha | Prueba | Resultado |
|---|---|---|
| 2026-08-04 | Smoke test stage: `POST /f2f/pinpad/v1/mobile/payment` con `Authorization: Basic` (token del comercio), `serial_number_pos` con prefijo `sdk-`, `amount` numérico | ✅ **200** — regresó `request_id` (`pinpad-<uuid>`) y `user_id` (cuenta dueña). Contrato idéntico al implementado |
| 2026-08-04 | Smoke test stage: `GET /f2f/pinpad/v1/payment?pinpadRequestId=` con `Pinpad-Include-Detail: false` y token del comercio | ✅ **200** — `status: IN_PROCESS`. Nota: falta validar que el BE regrese 40X cuando la sesión NO coincida con el `user_id` del request (pieza BE abierta) |
| 2026-08-04 | **E2E completo en dispositivo (demo + pinpad QA) — matriz decisiva** | ✅ **El candado funciona de punta a punta.** (a) **Misma cuenta** (dueña del token logueada en pinpad): `GET` → **200** → cobro fluye normal. (b) **Cuenta distinta** (caso cajero tranza): `GET` → **404 `PAYMENT_NOT_FOUND`** → cobro **bloqueado**, demo recibe `PAYMENT_REQUEST_OWNERSHIP_REJECTED`. Conclusión: **el BE ya resuelve ownership hoy** vía filtro por tenant (el request solo es visible para su dueño). Un resultado intermedio que sugería "404 hasta para el dueño" fue una confusión de cuentas durante las pruebas (corregido en F2F-843). En los logs se fotografió además el mecanismo del fraude: tras la validación, pinpad crea su propia orden con `user_id` del cajero — sin el gateo, ese es el request que se cobra |
| 2026-08-04 | Bug de UI detectado y corregido durante el E2E | El `Declined` emitido al bloquear no se colectaba (el `SDKChargeNavigationHandler` solo se monta en la pantalla de cierre) → pantalla colgada en "iniciando venta". Fix: `goToFinishTransaction()` tras emitir (el flow tiene `replay=1`) |
| 2026-08-04 | E2E: intent SDK → pinpad con variantes qa/dev | ⚠️ El SDK apuntaba fijo al package de release; con pinpad QA instalado (`.qa`) → `APPLICATION_NOT_FOUND` (entregado por listener, sin crash — fail-open OK). **Resuelto 2026-08-05**: el package se deriva del `ClipEnvironment` (STAGE → `.qa` / `debugPinpadPackage` opcional; PRODUCTION → release siempre) |

_Pendientes (F2F-841, en dispositivo): happy path end-to-end, mismatch de cuentas (⚠️ depende de la validación en BE), subcuenta válida, request expirado/usado, flujo sin request (SDK viejo), sin `initialize`, credenciales inválidas (401), modo avión, downgrade de versiones._

Notas del token (2026-08-04): las credenciales del portal pueden venir como token pre-armado `Basic base64(apiKey:secretKey)`; el SDK espera las dos partes por separado en `initialize(...)`. El token de stage usado en pruebas vive hardcodeado en `ClientDummyApplication.kt` de la demo — **no commitear**.

---

## 📦 Repos que tocamos

| Repo | Rol | Notas |
|------|-----|-------|
| `ClipMX/mobile.android.blaze.pinpad.sdk` | Principal (este repo) | SDK de PinPad para Android |
| `ClipMX/mobile.android.blaze.pinpad` | App receptora (analizada, no modificada) | App PinPad que recibe el intent del SDK. Ruta local: `../mobile.android.blaze.pinpad`. Módulos: `app` (com.payclip.blaze.pinpad) y `client` (demo que consume el SDK). Depende del SDK vía JitPack `com.github.ClipMX:mobile.android.blaze.pinpad.sdk:1.1.0` |

---

## 📝 Notas de sesión

### 2026-08-04

- Se definió el problema del cajero tranza y la solución de validación por payment request (ver "Objetivo" arriba). Tickets creados en Linear: F2F-834 (padre) + F2F-835…844, asignados a Adán.
- Se implementó la parte SDK completa (compila, sin commit). Hallazgos del BE (`pinpad-payments-api`, Kotlin/Spring Boot):
  - `POST /v1/mobile/payment` es el endpoint app-to-app (no inserta en Firebase); response trae `request_id` **y `user_id`** — el BE ya conoce la cuenta dueña del request.
  - Existe `GET /v1/payment?pinpadRequestId=` (status), con header `Pinpad-Include-Detail`; estados en `PaymentStatusType` (PENDING, IN_PROCESS, APPROVED, etc.).
  - La validación de API key vive en el gateway (C3), no en el servicio; internamente la autorización es por `merchant-id`.
  - Pinpad hoy se autentica con el JWT de la sesión (interceptor de `clip.commons`) — exactamente lo que el SDK NO replica.
- Este doc absorbió y reemplaza a `VALIDACION_PAGOS_APP_TO_APP.md` (repo pinpad, eliminado).

### 2026-07-30
- Sesión inicial: se configuró la bitácora de contexto del proyecto.
- Se analizó la app receptora `../mobile.android.blaze.pinpad`. Hallazgos clave del contrato SDK ↔ app:
  - **No hay intent-filter ni deep link**: el SDK lanza la app por componente explícito — `Intent(ACTION_MAIN)` hacia paquete `com.payclip.blaze.pinpad`, activity `com.payclip.blaze.pinpad.shared.ui.MainActivity` (SDK: `SystemClipIntentProvider.kt`), vía `startActivityForResult`. Si la app no está instalada → `ApplicationNotFoundException`.
  - **Extras de request**: `PAYMENT_REFERENCE`, `PAYMENT_AMOUNT` (String), `PAYMENT_TIP_AMOUNT`, `PAYMENT_AUTO_RETURN`, `PAYMENT_RETRY`, `PAYMENT_SHARE`, `REQUEST_PAYMENT_PREFERENCES` (JSON Gson), `CLIP_LOGIN_CREDENTIALS` (Parcelable `ClipPaymentLogin`), `PAYMENT_PREFERENCES` (Parcelable, deprecado).
  - **Respuesta**: siempre `setResult(RESULT_OK)` + extras `PAYMENT_RESPONSE` (JSON de `PaymentResult`, status `approved`/`error`), `PAYMENT_ERROR` (código), `LOGIN_SUCCESS_EXTRA`/`LOGIN_ERROR_EXTRA` (JSON de `LoginResult`). Result code distinto de OK → el SDK lo interpreta como `onCancelled()`.
  - **La app usa el propio SDK para leer/escribir el contrato** (`ClipIntentProviderFactory`, `ClipResultManagerFactory`) — ambos lados comparten las constantes del artefacto JitPack. Cambios al contrato en el SDK requieren actualizar la versión que consume la app.
  - Flujo en la app: `MainActivity` (Hilt/Compose) → si hay extras de pago navega a `SDKChargeScheme`/`SDKChargeViewModel` (módulo `order`) → flujo de cobro → `SDKChargeNavigationHandler` emite el resultado. Login por credenciales del SDK en `MainViewModel` + `SDKLoginNavigationHandler`; cancelación de sesión devuelve error `EMPTY_SESSION`.
  - La app también tiene un flujo "server-driven" independiente (Firebase Realtime Database, `FirebaseOrderSocket` + `OrderListenerService`) que no pasa por el intent del SDK.
  - Modelo espejo en la app: `PartialOrder` y su copia de `RequestPaymentPreferences` con campos extra (`redirectPackageName`, `tipOptions`) que el SDK aún no expone.
