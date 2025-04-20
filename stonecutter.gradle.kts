import dev.kikugie.stonecutter.build.StonecutterBuild
import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.ReleaseType
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.named

plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.10-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
    id("com.gradleup.shadow") version "8.3.5" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.7.+" apply false
}

stonecutter active "1.19.2" /* [SC] DO NOT EDIT */
stonecutter.automaticPlatformConstants = true

allprojects {
    repositories {
        mavenLocal()
        maven("https://maven.parchmentmc.org")
        maven("https://maven.shedaniel.me/")
        maven("https://mvn.devos.one/releases")
        maven("https://mvn.devos.one/snapshots")
    }
}

subprojects {
    if (project.extensions.findByName("stonecutter") == null)
        return@subprojects

    if (parent == rootProject)
        return@subprojects

    val sc = (project.extensions.getByName("stonecutter") as StonecutterBuild)
    val common = sc.node.sibling("")

    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "com.gradleup.shadow")

    val mcVersion = sc.current.version
    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    loom.silentMojangMappingsLicense()
    loom.decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    loom.mixin.useLegacyMixinAp = false

    dependencies {
        "minecraft"("com.mojang:minecraft:$mcVersion")
        "mappings"(loom.layered() {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$mcVersion:${common?.mod?.prop("parchment_snapshot") ?: mod.prop("parchment_snapshot")}")
        })
    }

    project.extensions.configure<JavaPluginExtension>("java") {
        withSourcesJar()
        val java = if (sc.eval(mcVersion, ">=1.20.5"))
            JavaVersion.VERSION_21 else JavaVersion.VERSION_17
        targetCompatibility = java
        sourceCompatibility = java
    }

    tasks.named<Jar>("jar") {
        archiveClassifier = "dev"
    }

    tasks.named<RemapJarTask>("remapJar") {
        injectAccessWidener = true
        input = tasks.named<Jar>("shadowJar").get().archiveFile
        archiveClassifier = null
        dependsOn(tasks.named<Jar>("shadowJar"))
    }

    val properLoaderName = when (project.property("loom.platform")) {
        "fabric" -> "Fabric"
        "forge" -> "Forge"
        "neoforge" -> "NeoForge"
        else -> ""
    }

    if (project.name.contains("fabric") || project.name.contains("forge")) {
        apply(plugin = "me.modmuss50.mod-publish-plugin")

        project.extensions.configure<ModPublishExtension>("publishMods") {
            file = tasks.named<RemapJarTask>("remapJar").get().archiveFile
            displayName = "TalkBalloons ${project.property("mod.version")}+${mcVersion} ($properLoaderName)"
            version = project.property("mod.version").toString()
            changelog = rootProject.file("CHANGELOG.md").readText()
            type = ReleaseType.BETA
            modLoaders.add(project.property("loom.platform").toString())

            dryRun = providers.environmentVariable("MODRINTH_TOKEN")
                .getOrNull() == null || providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null

            modrinth {
                projectId = property("publish.modrinth").toString()
                accessToken = providers.environmentVariable("MODRINTH_TOKEN")
                minecraftVersions.add(mcVersion)
                requires {
                    slug = "fabric-api"
                }
                requires {
                    slug = "cloth-config"
                }
            }

            curseforge {
                projectId = property("publish.curseforge").toString()
                accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
                minecraftVersions.add(mcVersion)
                requires {
                    slug = "fabric-api"
                }
                requires {
                    slug = "cloth-config"
                }
            }
        }
    }
}

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("buildAndCollect")
}

// Builds loader-specific versions into `build/libs/{mod.version}/{loader}`
for (it in stonecutter.tree.branches) {
    if (it.id.isEmpty()) continue
    val loader = it.id.upperCaseFirst()
    stonecutter registerChiseled tasks.register("chiseledBuild$loader", stonecutter.chiseled) {
        group = "project"
        versions { branch, _ -> branch == it.id }
        ofTask("buildAndCollect")
    }
}

// Runs active versions for each loader
for (it in stonecutter.tree.nodes) {
    if (it.metadata != stonecutter.current || it.branch.id.isEmpty()) continue
    val types = listOf("Client", "Server")
    val loader = it.branch.id.upperCaseFirst()
    for (type in types) it.tasks.register("runActive$type$loader") {
        group = "project"
        dependsOn("run$type")
    }
}
