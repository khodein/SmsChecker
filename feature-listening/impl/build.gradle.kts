plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.listening"
}

dependencies {
    implementation(project(":feature-listening:api"))
    implementation(project(":feature-sms:api"))
    implementation(project(":feature-email:api"))
    implementation(project(":feature-settings:api"))
    implementation(project(":feature-warning:api"))
}
