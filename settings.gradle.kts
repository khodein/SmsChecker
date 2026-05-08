pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SmsChecker"

fun includeProjectModules() {
    rootDir
        .listFiles()
        .orEmpty()
        .asSequence()
        .filter { it.isDirectory }
        .filter { it.name != "build-logic" }
        .filter { it.resolve("build.gradle.kts").isFile }
        .sortedBy { it.name }
        .forEach { moduleDir ->
            include(":${moduleDir.name}")
        }
}

includeProjectModules()
