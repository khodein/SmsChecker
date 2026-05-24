plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.telegram"
}

dependencies {
    implementation(project(":feature-telegram:api"))
}
