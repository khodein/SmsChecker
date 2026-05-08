import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

plugins {
    id("smschecker.android.library")
    id("smschecker.android.core")
    id("smschecker.android.compose")
    id("smschecker.koin")
    id("smschecker.ktor")
    id("smschecker.coil")
    id("smschecker.android.room")
    id("smschecker.android.datastore")
    id("smschecker.detekt")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addFeatureDependencies() {
    add("implementation", libs.findLibrary("androidx-navigation3-runtime").get())
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addFeatureDependencies()
}
