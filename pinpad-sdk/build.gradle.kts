plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
    id("kotlin-parcelize")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.payclip.blaze.pinpad.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 22

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
    }
}

val sourcesJar: Jar = tasks.create("sourcesJar", Jar::class.java) {
    archiveClassifier.set("sources")
    from("src/main/java")
}

project.artifacts {
    archives(sourcesJar)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.ClipMX"
                artifactId = "mobile.android.blaze.pinpad.sdk"
                // the version is provided by the TAG name
                version = System.getenv("GITHUB_REF_NAME") ?: "0.0.0"

                afterEvaluate {
                    from(components["release"])
                    artifact(project.tasks.getByName("sourcesJar"))
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.coreKtx)

    testImplementation(libs.junit4)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.activity.compose)

    implementation(libs.retrofitCore)
    implementation(libs.retrofit.converterGson)
    implementation(libs.gsonCore)
    implementation(libs.okhttpCore)
    implementation(libs.okhttpLogging)
}
