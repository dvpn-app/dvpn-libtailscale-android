plugins {
    id("convention.publish-android-library-jitpack")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

dependencies {
    api(files("${rootDir}/libs/libtailscale.aar"))
    implementation(libs.androidxCoreKtx)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinSerialization)
    implementation(libs.okhttp)
    implementation(libs.timber)
}

val extractAarForPublishing = tasks.register<Copy>("extractAarForPublishing") {
    from(zipTree("${rootDir}/libs/libtailscale.aar"))
    into("${layout.buildDirectory.get()}/intermediates/extracted-aar")
}

tasks.whenTaskAdded {
    if (name == "bundleReleaseAar") {
        dependsOn(extractAarForPublishing)
    }
}

afterEvaluate {
    tasks.named("bundleReleaseAar") {
        doFirst {
            copy {
                from("${layout.buildDirectory.get()}/intermediates/extracted-aar/classes.jar")
                into("${layout.buildDirectory.get()}/intermediates/aar_main_jar/release")
            }

            copy {
                from("${layout.buildDirectory.get()}/intermediates/extracted-aar/jni")
                into("${layout.buildDirectory.get()}/intermediates/library_jni/release/jni")
            }
        }
    }
}
