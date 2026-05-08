plugins {
    id("smschecker.android.application")
    id("smschecker.android.core")
    id("smschecker.android.compose")
    id("smschecker.android.navigation3")
    id("smschecker.koin")
    id("smschecker.ktor")
    id("smschecker.coil")
    id("smschecker.android.room")
    id("smschecker.android.datastore")
    id("smschecker.android.test")
    id("smschecker.detekt")
}

android {
    namespace = "com.sms.checker.forwarder"

    defaultConfig {
        applicationId = "com.sms.checker.forwarder"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":framework"))
    implementationFeatureModules()
}

fun DependencyHandlerScope.implementationFeatureModules() {
    rootProject.subprojects
        .asSequence()
        .filter { it.name.startsWith("feature-") }
        .sortedBy { it.name }
        .forEach { featureProject ->
            implementation(project(":${featureProject.name}"))
        }
}
