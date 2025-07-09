plugins {
    id("convention.android-library")
    `maven-publish`
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
                version = "0.1.0"
                
                pom {
                    name.set(project.name)
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
                }
            }
        }
    }
}

tasks.withType<Javadoc> {
    isFailOnError = false
    options {
        (this as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
            addStringOption("charSet", "UTF-8")
        }
    }
} 