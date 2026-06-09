plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.email"
}

dependencies {
    implementation(project(":feature-email:api"))
    implementation(project(":feature-sms:api"))

    implementation(libs.android.mail)
    implementation(libs.android.activation)
}
