val ktorVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.dokka") version "2.1.0"
    kotlin("plugin.serialization") version "2.2.20"
    `java-library`
    `maven-publish`
    signing
}

group = "net.blophy.nova"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-client-logging:${ktorVersion}")
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")
    testImplementation("io.ktor:ktor-client-mock:${ktorVersion}")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

// 生成源码 jar
tasks.withType<Jar> {
    // 可选：如果希望源码包也包含 Kotlin 源码
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("kollama")
                description.set("A Kotlin-First Ollama Client with Full Type Safety")
                url.set("https://github.com/BlophyNova/kollama")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("MojaveHao")
                        name.set("MojaveHao")
                        email.set("MojaveHao")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/BlophyNova/kollama.git")
                    developerConnection.set("scm:git:ssh://github.com:BlophyNova/kollama.git")
                    url.set("https://github.com/BlophyNova/kollama")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = System.getenv("MVNC_USERNAME")
                password = System.getenv("MVNC_PASSWORD")
            }
        }
    }
}

signing {
    val keyId = System.getenv("SIGNING_KEY_ID")
    val password = System.getenv("SIGNING_PASSWORD")

    if (keyId != null && password != null) {
        useInMemoryPgpKeys(keyId, password)
    }
    sign(publishing.publications["mavenJava"])
}

dokka {
    dokkaPublications.html {
        moduleName.set(project.name)
        moduleVersion.set(project.version.toString())
        // Standard output directory for HTML documentation
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        failOnWarning.set(false)
        suppressInheritedMembers.set(false)
        suppressObviousFunctions.set(true)
        offlineMode.set(false)
        includes.from("README.md")
    }
    pluginsConfiguration.html {
        footerMessage.set("© 2026 Copyright Lime Network")
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
