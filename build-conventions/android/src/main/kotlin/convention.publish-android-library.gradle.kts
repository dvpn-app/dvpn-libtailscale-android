plugins {
    id("convention.android-library")
    `maven-publish`
    signing
}

group = "app.dvpn.libtailscale"
version = "0.1.0"

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                
                groupId = "app.dvpn.libtailscale"
                artifactId = project.name
                version = project.version.toString()
                
                pom {
                    name.set("Libtailscale Android")
                    description.set("Libtailscale wrapper for Android")
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
                    
                    scm {
                        connection.set("scm:git:git://github.com/dvpn-app/dvpn-libtailscale-android.git")
                        developerConnection.set("scm:git:ssh://github.com/dvpn-app/dvpn-libtailscale-android.git")
                        url.set("https://github.com/dvpn-app/dvpn-libtailscale-android")
                    }
                }
            }
        }
        
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/dvpn-app/dvpn-libtailscale-android")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
    
    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}

tasks.withType<Javadoc> {
    options {
        (this as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
            addStringOption("charSet", "UTF-8")
        }
    }
} 