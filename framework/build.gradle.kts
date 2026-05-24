plugins {
    id("smschecker.android.library")
    id("smschecker.android.core")
    id("smschecker.android.compose")
    id("smschecker.detekt")
}

android {
    namespace = "com.sms.checker.forwarder.framework"
}

dependencies {
    implementation(project(":framework:tools"))
}
