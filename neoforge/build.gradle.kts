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
    neoForge()
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
    get("developmentNeoForge").extendsFrom(commonBundle)
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    "neoForge"("net.neoforged:neoforge:${common.mod.dep("neoforge_loader")}")
    "io.github.llamalad7:mixinextras-neoforge:${mod.dep("mixin_extras")}".let {
        implementation(it)
        include(it)
    }

    modImplementation("me.shedaniel.cloth:cloth-config-neoforge:${common.mod.dep("cloth_config")}")

    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionNeoForge")) { isTransitive = false }

    modImplementation("thedarkcolour:kotlinforforge-neoforge:${common.mod.dep("kotlinforforge")}")

    if (stonecutter.eval(minecraft, ">=1.20")) {
        // i'm not kidding when I say this was the only way it would run in my dev env. wtf.
        modRuntimeOnly("thedarkcolour:kfflib-neoforge:${common.mod.dep("kotlinforforge")}")
        modRuntimeOnly("thedarkcolour:kffmod-neoforge:${common.mod.dep("kotlinforforge")}")
        modRuntimeOnly("thedarkcolour:kfflang-neoforge:${common.mod.dep("kotlinforforge")}")
    }

    modImplementation("xyz.bluspring.modernnetworking:modernnetworking-neoforge:${common.mod.dep("modernnetworking")}+${common.mod.dep("modernnetworking_mc")}")!!
}

loom {
    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
        vmArgs("-Dmixin.debug.export=true", "-XX:+AllowEnhancedClassRedefinition")
    }
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
    exclude("fabric.mod.json", "architectury.common.json")
}

tasks.processResources {
    properties(listOf("META-INF/neoforge.mods.toml", "META-INF/mods.toml"),
        "mod_id" to mod.id,
        "mod_name" to mod.name,
        "mod_version" to mod.version,
        "mod_description" to mod.prop("description"),
        "mod_authors" to mod.prop("authors"),
        "minecraft_version_range" to common.mod.prop("mc_dep_forgelike"),
        "neoforge_version" to common.mod.dep("neoforge_loader"),
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