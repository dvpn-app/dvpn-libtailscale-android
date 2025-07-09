plugins {
    id("convention.publish-android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

dependencies {
    api(fileTree("${rootDir}/libs") {
        setIncludes(listOf("*aar", "*.jar"))
    })
    implementation(libs.androidxCoreKtx)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinSerialization)
    implementation(libs.okhttp)
    implementation(libs.timber)
}
