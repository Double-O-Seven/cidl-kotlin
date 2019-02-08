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
    maven
    kotlin("jvm") version "1.3.11"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

repositories {
    mavenCentral()
}

val gitVersion: Closure<String> by extra

version = gitVersion()

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

}

dependencies {

    antlr("org.antlr", "antlr4", "4.7.1")

    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.3.11")

    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.3.11")
    implementation("org.antlr", "antlr4-runtime", "4.7.1")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.4.0")
    implementation("org.junit.jupiter", "junit-jupiter-params", "5.4.0")
    implementation("io.mockk", "mockk", "1.8.4")
    implementation("org.assertj", "assertj-core", "3.10.0")

    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.4.0")
}

