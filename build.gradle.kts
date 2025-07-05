plugins {
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.composeCompiler) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath(libs.pluginAndroidGradle)
        classpath(libs.pluginKotlinGradle)
        classpath(libs.pluginNavigationArgs)
    }

    allprojects {
        repositories {
            google()
            mavenCentral()
            maven { setUrl("https://jitpack.io") }
        }
    }
}
