@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
}

val loader = prop("loom.platform")!!
val minecraft: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")?.project) {
    "No common project for $project"
}

version = "${mod.version}+$minecraft"
base {
    archivesName.set("${mod.id}-$loader")
}
architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge {
        convertAccessWideners = true
        mixinConfigs("${mod.id}-common.mixins.json")
        mixinConfigs("${mod.id}.mixins.json")
    }
}

val commonBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations {
    compileClasspath.get().extendsFrom(commonBundle)
    runtimeClasspath.get().extendsFrom(commonBundle)
    get("developmentForge").extendsFrom(commonBundle)
}

repositories {
    maven("https://maven.minecraftforge.net")
}

private fun overridableModDep(dep: String): String {
    return if (mod.dep(dep) != "[VERSIONED]")
        mod.dep(dep)
    else
        common.mod.dep(dep)
}

dependencies {
    "forge"("net.minecraftforge:forge:$minecraft-${common.mod.dep("forge_loader")}")
    "io.github.llamalad7:mixinextras-forge:${mod.dep("mixin_extras")}".let {
        implementation(it)
        include(it)
    }


    modImplementation("me.shedaniel.cloth:cloth-config-forge:${overridableModDep("cloth_config")}")

    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionForge")) { isTransitive = false }

    modImplementation("xyz.bluspring.modernnetworking:modernnetworking-forge:${overridableModDep("modernnetworking")}+${overridableModDep("modernnetworking_mc")}")!!
    modImplementation("thedarkcolour:kotlinforforge:${common.mod.dep("kotlinforforge")}")

    implementation(include("io.github.llamalad7:mixinextras-forge:0.4.1")!!)
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
    exclude("fabric.mod.json", "architectury.common.json")
}

tasks.processResources {
    properties(listOf("META-INF/mods.toml"),
        "mod_id" to mod.id,
        "mod_name" to mod.name,
        "mod_version" to mod.version,
        "mod_description" to mod.prop("description"),
        "mod_authors" to mod.prop("authors"),
        "minecraft_version_range" to common.mod.prop("mc_dep_forgelike"),
        "forge_version" to common.mod.dep("forge_loader"),
        "cloth_config_version" to common.mod.dep("cloth_config"),
        "modernnetworking_version" to common.mod.dep("modernnetworking")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}