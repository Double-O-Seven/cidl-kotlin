import groovy.lang.Closure

buildscript {
    repositories {
        mavenCentral()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(group = "org.junit.platform", name = "junit-platform-gradle-plugin", version = "1.0.0")
    }
}

group = "ch.leadrian.samp.kamp"

plugins {
    antlr
    java
    `java-library`
    maven
    `maven-publish`
    signing
    kotlin("jvm") version "1.3.11"
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

repositories {
    mavenCentral()
}

val gitVersion: Closure<String> by extra

version = gitVersion()

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.dokka)
    archiveClassifier.set("javadoc")
}

tasks {

    compileKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
        }
        dependsOn(generateGrammarSource)
    }

    compileTestKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    generateGrammarSource {
        maxHeapSize = "64m"
        arguments = listOf("-visitor", "-long-messages")
    }

    dokka {
        reportUndocumented = false
    }

}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("CIDL Kotlin")
                description.set("The Java/Kotlin implementation of https://github.com/Zeex/cidl")
                url.set("https://github.com/Double-O-Seven/cidl-kotlin")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Double-O-Seven")
                        name.set("Adrian-Philipp Leuenberger")
                        email.set("thewishwithin@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Double-O-Seven/cidl-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/Double-O-Seven/cidl-kotlin.git")
                    url.set("https://github.com/Double-O-Seven/cidl-kotlin")
                }
            }
        }
    }
    repositories {
        maven {
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                val ossrhUsername: String? by extra
                val ossrhPassword: String? by extra
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

dependencies {

    antlr("org.antlr", "antlr4", "4.7.1")

    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.3.11")

    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.3.11")
    implementation("org.antlr", "antlr4-runtime", "4.7.1")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.4.0")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.4.0")
    testImplementation("io.mockk", "mockk", "1.8.4")
    testImplementation("org.assertj", "assertj-core", "3.10.0")

    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.4.0")
}
