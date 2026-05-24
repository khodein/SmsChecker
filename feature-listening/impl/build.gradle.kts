plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.listening"
}

dependencies {
    implementation(project(":feature-listening:api"))
    implementation(project(":feature-sms:api"))
}
