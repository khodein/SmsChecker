plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.warning"
}

dependencies {
    implementation(project(":feature-warning:api"))
}
