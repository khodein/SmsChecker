plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.settings"
}

dependencies {
    implementation(project(":feature-settings:api"))
}
