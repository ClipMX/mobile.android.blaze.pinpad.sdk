# ANEXO B: infraestructura, CI/CD y distribución

## CI/CD (`.github/workflows/`)

| Workflow | Disparo | Qué hace | Permisos y secrets |
|---|---|---|---|
| `codeql.yml` | `push`, `pull_request` (`:14-17`) | análisis CodeQL con `actions/checkout@v4` (`:41`), `github/codeql-action/init@v4` (`:45`), `actions/setup-java@v4` (`:71`), `gradle/actions/setup-gradle@v6` (`:82`), `codeql-action/analyze@v4` (`:87`). El `autobuild@v2` está comentado (`:59`) | `permissions` en `:24`; sin secrets |
| `dependecy-review.yml` | `pull_request` (`:2`) | `actions/checkout@v4` (`:11`) y `actions/dependency-review-action@v4` (`:13`) | `permissions` en `:7` |

Observaciones:

- No hay workflow de publicación: JitPack construye desde el tag del integrador (ver más abajo).
- Ninguna acción está fijada a SHA.
- `.github/workflows/dependecy-review.yml`: el nombre del archivo tiene una errata (`dependecy`). No afecta al funcionamiento.

## Build

- Gradle wrapper (`gradle/wrapper/gradle-wrapper.properties`).
- `gradle/libs.versions.toml`: Kotlin 2.4.10, AGP 9.3.0, Compose BOM 2024.02.02, Retrofit 2.9.0, OkHttp 4.11.0, Gson 2.10.1, JUnit 4.13.2, MockK 1.13.7, Mockito-Kotlin 5.1.0, coroutines-test 1.7.3, `jvmTarget = 11`.
- `settings.gradle.kts`: `pluginManagement` y `dependencyResolutionManagement` con `FAIL_ON_PROJECT_REPOS`. Repos: `google()`, `mavenCentral()`, `mavenLocal()`, `gradlePluginPortal()`. **No aplica settings remotos.**
- `pinpad-sdk/build.gradle.kts`:
  - `namespace = com.payclip.blaze.pinpad.sdk`, `compileSdk = 34`, `minSdk = 22`.
  - `buildConfigField`: `CLIP_APP_SUFFIX` (de `clipAppSuffix`) y `SDK_VERSION` (de `GITHUB_REF_NAME` o `VERSION`).
  - `publishing { singleVariant("release") }`, publicación Maven con `groupId = com.github.ClipMX`, `artifactId = mobile.android.blaze.pinpad.sdk`, `version = sdkVersion + qaVersionSuffix`.
  - `consumerProguardFiles("consumer-rules.pro")` (`:25`). Sin `minifyEnabled` en el módulo de librería (no aplica).
  - Compilación: `-Xjvm-default=all` para que los defaults de `PaymentListener` sean visibles desde Java.
- `jitpack.yml`: JDK `openjdk18`, instala `18.0.2-tem` con SDKMAN.

## Distribución

- **JitPack**: `com.github.ClipMX:mobile.android.blaze.pinpad.sdk:<tag>`. JitPack construye desde el tag de Git. Sin firma de artefacto en el repo.
- **mavenLocal**: versión `<versión>-qa` para pruebas internas (`publishToMavenLocal -PclipAppSuffix=.qa`), explícitamente no publicada.
- **Play Store / Firebase App Distribution**: no aplica (es una librería).

## Recursos AWS / Terraform

No aplica. El Server Side SDK corre en la infraestructura de Clip (`api.payclip.io`), fuera de este repo.
