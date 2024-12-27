pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven {
            val gradlePropertiesFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")
            val properties = java.util.Properties()

            if (gradlePropertiesFile.exists()) {
                gradlePropertiesFile.inputStream().use { input -> properties.load(input) }
            }

            val user = properties.getProperty("github.user")
            val token = properties.getProperty("github.token")

            url = uri("https://maven.pkg.github.com/ClipMX/*")

            credentials {
                username = System.getenv("PACKAGES_USERNAME") ?: user
                password = System.getenv("PACKAGES_TOKEN") ?: token
            }
        }
    }
}

val user = System.getenv("PACKAGES_USERNAME") ?: findInGradleProperties("github.user")
val token = System.getenv("PACKAGES_TOKEN") ?: findInGradleProperties("github.token")

fun findInGradleProperties(env: String): String? {
    val gradlePropertiesFile = File(System.getProperty("user.home"), ".gradle/gradle.properties")
    return if (gradlePropertiesFile.exists()) {
        val properties = java.util.Properties()
        gradlePropertiesFile.inputStream().use { input -> properties.load(input) }
        properties.getProperty(env)
    } else {
        null
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/ClipMX/*")
            credentials {
                username = user
                password = token
            }
        }

        versionCatalogs {
            create("clipLibs") {
                from("com.payclip.blaze:versions-catalog:0.6.0")
            }
        }
    }
}

include(":pinpad-sdk")

rootProject.name = "PinPad SDK"
