import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion

plugins {
    id("com.android.application")
}

val appVersionMajor = providers.gradleProperty("app.version.major").get().toInt()
val appVersionMinor = providers.gradleProperty("app.version.minor").get().toInt()
val appVersionPatch = providers.gradleProperty("app.version.patch").get().toInt()
val isReleaseMinifyEnabled = providers.gradleProperty("app.release.minify")
    .map(String::toBoolean)
    .getOrElse(false)

extensions.configure<ApplicationExtension>("android") {
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "$appVersionMajor.$appVersionMinor.$appVersionPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = isReleaseMinifyEnabled
            isShrinkResources = isReleaseMinifyEnabled
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
