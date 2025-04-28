plugins {
    alias(clipLibs.plugins.android.library)
    alias(libs.plugins.blaze.configuration)
    id("kotlin-parcelize")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.payclip.blaze.pinpad.sdk"

    kotlinOptions {
        jvmTarget = clipLibs.versions.jvmTarget.get()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = clipLibs.versions.androidxComposeCompiler.get()
    }
}

val sourcesJar: Jar = tasks.create("sourcesJar", Jar::class.java) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

project.artifacts {
    archives(sourcesJar)
}

dependencies {
    implementation(clipLibs.androidx.coreKtx)

    compileOnly(clipLibs.blaze.lintChecks)

    // Test
    testImplementation(clipLibs.junit4)
    testImplementation(clipLibs.mockito.kotlin)
    testImplementation(clipLibs.mockk)
    testImplementation(clipLibs.coroutines.test)
    androidTestImplementation(clipLibs.androidx.test.ext)
    androidTestImplementation(clipLibs.androidx.test.espressoCore)
    testImplementation(clipLibs.androidx.room.testing)

    // Compose
    implementation(platform(clipLibs.androidx.compose.bom))
    implementation(clipLibs.androidx.compose.runtime.livedata)
    implementation(clipLibs.androidx.compose.material3)
    implementation(clipLibs.androidx.compose.ui)
    implementation(clipLibs.androidx.compose.ui.graphics)
    implementation(clipLibs.androidx.compose.ui.tooling)
    implementation(clipLibs.androidx.compose.ui.toolingPreview)
    implementation(clipLibs.androidx.compose.navigation)
    implementation(clipLibs.androidx.lifecycle.runtimeCompose)
    implementation(clipLibs.androidx.lifecycle.viewModelCompose)
    implementation(clipLibs.androidx.activity.compose)

    // Retrofit
    implementation(clipLibs.retrofitCore)
    implementation(clipLibs.retrofit.converterGson)
    implementation(clipLibs.gsonCore)
    implementation(clipLibs.okhttpCore)
    implementation(clipLibs.okhttpLogging)
}
