import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    alias(libs.plugins.fletching.table)
    alias(libs.plugins.shadow)
    `maven-publish`
}

if (shouldRemap()) {
    apply(plugin = "net.fabricmc.fabric-loom-remap")
} else {
    apply(plugin = "net.fabricmc.fabric-loom")
}

setupCommon("fabric")
setupCommonLoom("fabric")

val loom = extensions.getByType<LoomGradleExtensionAPI>()

val common = stonecutter.node.sibling("")?.project
val shadedDep by configurations.named("shadedDep")

dependencies {
    moddedImplementation(libs.fabric.loader)
    moddedApi(libs.fabric.kotlin)
    moddedApi("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api", common?.project?.mod?.dep("fabric_api"))}")
    moddedImplementation("com.terraformersmc:modmenu:${mod.dep("modmenu", common?.project?.mod?.dep("modmenu"))}")
    api(libs.mixinextras.fabric)
    annotationProcessor(libs.mixinextras.fabric)
    moddedApi(libs.sunset)
    "include"(libs.sunset)
    "include"(moddedApi("dev.isxander:yet-another-config-lib:${mod.dep("yacl", common?.project?.mod?.dep("yacl"))}-fabric")!!)
}
