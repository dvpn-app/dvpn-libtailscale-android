plugins {
    id("convention.android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

dependencies {
    implementation(fileTree("libs") {
        setIncludes(listOf("*aar", "*.jar"))
    })

    api(libs.bundles.retrofit)
    api(libs.kotlinCoroutinesCore)
    api(libs.kotlinSerialization)
    api(libs.okhttp)
    api(libs.okhttpInterceptor)
}
