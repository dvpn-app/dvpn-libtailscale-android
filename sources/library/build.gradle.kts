plugins {
    id("convention.publish-android-library-jitpack")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

dependencies {
    api(projects.sources.libtailscale)
    implementation(libs.androidxCoreKtx)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinSerialization)
    implementation(libs.okhttp)
    implementation(libs.timber)
}
