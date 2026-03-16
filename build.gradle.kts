plugins {
    id("com.gradleup.shadow") version "9.3.1" // https://github.com/GradleUp/shadow
    id("io.freefair.lombok") version "9.2.0"
    id("xyz.jpenilla.run-paper") version "3.0.2" // https://github.com/jpenilla/run-task
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6FireAlarms"

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

tasks {
    build {
        dependsOn("shadowJar")
        dependsOn("processResources")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    shadowJar {
        archiveBaseName.set("World1-6FireAlarms")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    runServer {
        minecraftVersion("1.21.11")

        downloadPlugins {
            url("https://github.com/World1-6/World1-6Utils/releases/download/latest/World1-6Utils.jar")
        }
    }
}

repositories {
    mavenLocal()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:02ceaacb83")
    compileOnly("org.geysermc.floodgate:api:2.2.5-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("shadowJar"))
        }
    }
}