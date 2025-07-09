package buildsrc.convention

plugins {
    `maven-publish`
    signing
    id("convention.kotlin-jvm")
}

group = "app.dvpn.libtailscale"
version = "0.1.0"
description = "Libtailscale wrapper for Android"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("Twitter Client Kotlin")
                description.set(project.description ?: "Libtailscale wrapper for Android")
                url.set("https://github.com/dvpn-app/dvpn-libtailscale-android")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("zeroxpunk")
                        name.set("punk")
                        email.set("punk.dvpn@proton.me")
                    }
                }
            }
        }
    }
}
