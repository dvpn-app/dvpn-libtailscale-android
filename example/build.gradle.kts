plugins {
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.1.21"
    id("convention.android-application")
}

android.namespace = "dvpn.libtailscale.example"

dependencies {
    implementation(projects.sources.library)
    implementation(libs.androidxAppcompat)
    implementation(libs.androidxActivityKtx)
    implementation(libs.androidXWorker)
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidBrowserHelper)
    implementation(libs.kotlinSerialization)
    api(libs.androidxActivityCompose)
    api(libs.androidxComposeAnimation)
    api(libs.androidxComposeMaterial)
    api(libs.androidxComposeMaterial3)
    api(libs.androidxComposeRuntime)
    api(libs.androidxComposeUi)
    api(libs.androidxComposeUiTooling)
    api(libs.androidxComposeUiToolingPreview)
    api(libs.composeShimmer)
    api(libs.androidxComposeFoundation)
    api(platform(libs.androidxComposeBom))
    implementation(libs.material)
    implementation(libs.koinAndroid)
    implementation(libs.koinCompose)
    implementation(libs.timber)
}
