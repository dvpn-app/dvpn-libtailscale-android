plugins {
    id("convention.android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

dependencies {
    implementation(fileTree("${rootDir}/libs") {
        setIncludes(listOf("*aar", "*.jar"))
    })
    implementation(libs.androidxCoreKtx)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinSerialization)
    implementation(libs.okhttp)
    implementation(libs.timber)
}
