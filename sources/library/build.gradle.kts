plugins {
    id("convention.publish-android-library-jitpack")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "app.dvpn.libtailscale"

val localAar = configurations.create("localAar")

dependencies {
    localAar(files("${rootDir}/libs/libtailscale.aar"))
    
    // Regular dependencies
    implementation(libs.androidxCoreKtx)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinSerialization)
    implementation(libs.okhttp)
    implementation(libs.timber)
}

val extractAar = tasks.register<Copy>("extractAar") {
    from(zipTree(configurations["localAar"].singleFile))
    into("${buildDir}/extracted-aar")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(extractAar)
}

android {
    sourceSets {
        getByName("main") {
            java.srcDir("${buildDir}/extracted-aar/classes")
            res.srcDir("${buildDir}/extracted-aar/res")
            assets.srcDir("${buildDir}/extracted-aar/assets")
            jniLibs.srcDir("${buildDir}/extracted-aar/jni")
        }
    }
}

afterEvaluate {
    tasks.named("preBuild").configure {
        dependsOn(extractAar)
    }
    dependencies {
        compileOnly(files("${buildDir}/extracted-aar/classes.jar"))
    }
}
