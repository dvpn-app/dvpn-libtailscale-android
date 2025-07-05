plugins {
    `kotlin-dsl`
}

group = "app.dvpn.libtailscale.build-conventions"

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location)) // Workaround for https://github.com/gradle/gradle/issues/15383
    implementation(libs.pluginAndroidGradle)
    implementation(libs.pluginKotlinGradle)
    implementation(projects.environment)
    implementation(projects.kotlin)
    implementation(projects.util)
}
