plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.sms"
}

dependencies {
    implementation(project(":feature-sms:api"))
    implementation(project(":feature-listening:api"))
    implementation(project(":feature-warning:api"))
}
