rootProject.name = "dvpnlibtailscaleandroid"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
    pluginManagement {
        repositories {
            google()
            mavenCentral()
            maven { setUrl("https://plugins.gradle.org/m2/") }
        }
    }
}

includeBuild("build-settings")
includeBuild("build-conventions")

include(
    ":sources:library",
    ":sources:libtailscale",

    ":example"
)
