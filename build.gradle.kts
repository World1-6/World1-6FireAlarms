plugins {
    id("io.freefair.lombok") version "9.1.0" // https://plugins.gradle.org/plugin/io.freefair.lombok
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0-SNAPSHOT"
description = "World1-6FireAlarms"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveFileName.set("World1-6FireAlarms.jar")
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://repo.opencollab.dev/maven-releases/")
    }

    maven {
        url = uri("https://repo.opencollab.dev/maven-snapshots/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:366d29bf7c")
    compileOnly("org.geysermc.floodgate:api:2.2.5-SNAPSHOT")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}