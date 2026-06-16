plugins {
    id("smschecker.android.library")
    id("smschecker.android.core")
    id("smschecker.android.compose")
    id("smschecker.detekt")
    id("smschecker.android.navigation3")
}

android {
    namespace = "com.sms.checker.forwarder.framework"
}

dependencies {
    implementation(project(":framework:tools"))
}
