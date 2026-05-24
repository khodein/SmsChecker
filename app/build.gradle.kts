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
    implementation(project(":framework:tools"))
    implementation(project(":router"))
    implementationFeatureModules()
}

fun DependencyHandlerScope.implementationFeatureModules() {
    rootProject.subprojects
        .asSequence()
        .filter { project ->
            val isFeatureSubmodule = (project.name == "impl" || project.name == "api") &&
                project.parent?.name?.startsWith("feature-") == true
            val isLegacyFeature = project.name.startsWith("feature-") && project.childProjects.isEmpty()
            isFeatureSubmodule || isLegacyFeature
        }
        .sortedBy { it.path }
        .forEach { implementation(project(it.path)) }
}
