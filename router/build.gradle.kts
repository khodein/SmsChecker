plugins {
    id("smschecker.android.library")
    id("smschecker.detekt")
}

android {
    namespace = "com.sms.checker.forwarder.router"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
