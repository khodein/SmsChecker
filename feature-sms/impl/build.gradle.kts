plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.sms"
}

dependencies {
    implementation(project(":feature-sms:api"))
}
