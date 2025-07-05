plugins {
    `kotlin-dsl`
}

group = "app.dvpn.libtailscale.build-conventions"

dependencies {
    implementation(libs.pluginKotlinGradle)
    implementation(projects.environment)
}
