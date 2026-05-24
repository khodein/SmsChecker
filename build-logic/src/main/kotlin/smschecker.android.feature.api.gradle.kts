plugins {
    id("smschecker.android.library")
    id("smschecker.android.core")
    id("smschecker.detekt")
}

pluginManager.withPlugin("com.android.library") {
    dependencies.add("implementation", project(":framework:tools"))
}
