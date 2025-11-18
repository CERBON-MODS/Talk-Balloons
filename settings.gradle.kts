pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version("0.7.+")
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true

    val versions = listOf("1.18.2", "1.19.2", "1.20.1", "1.20.4", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.10")

    create(rootProject) {
        versions(versions)
        vcsVersion = "1.20.1"

        branch("fabric")
        branch("forge") {
            versions(versions.filterIndexed { i, _ -> i <= versions.indexOf("1.20.4") })
        }
        branch("neoforge") {
            versions(versions.filterIndexed { i, _ -> i >= versions.indexOf("1.20.4") })
        }
    }
}

rootProject.name = "TalkBalloons"