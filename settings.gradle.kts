pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("dev.kikugie.stonecutter") version "0.9.+" // https://stonecutter.kikugie.dev/
}

val versions = listOf("1.21.1", "1.21.4", "1.21.5", "1.21.8", "1.21.10", "1.21.11", "26.1.2")

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true

    create(rootProject) {
        versions(versions)
        vcsVersion = "1.21.1"

        branch("common")
        branch("fabric")
//        branch("forge") {
//            versions(versions.filter { stonecutter.eval(it, "<=1.20.4") })
//        }
        branch("neoforge") {
            versions(versions.filter { stonecutter.eval(it, ">=1.20.4") })
        }
    }
}

rootProject.name = "TalkBalloons"
