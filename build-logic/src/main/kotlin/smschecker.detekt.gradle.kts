import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

plugins {
    id("dev.detekt")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

detekt {
    toolVersion = libs.findVersion("detekt").get().requiredVersion
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    parallel = true
    basePath = rootProject.layout.projectDirectory
}

dependencies {
    detektPlugins(libs.findLibrary("detekt-rules-ktlint-wrapper").get())
}

tasks.withType<Detekt>().configureEach {
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/.gradle/**")
    exclude("**/.kotlin/**")
    exclude("**/generated/**")
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/.gradle/**")
    exclude("**/.kotlin/**")
    exclude("**/generated/**")
}
