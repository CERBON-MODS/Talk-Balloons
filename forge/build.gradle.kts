plugins {
    alias(libs.plugins.moddevgradle.legacy)
    alias(libs.plugins.shadow)
}

val mcVersion = stonecutter.current.version
val common = stonecutter.node.sibling("")!!

legacyForge {
    version = "$mcVersion-${mod.dep("forge", common.project.mod.dep("forge"))}"

    configureModDev(this, "forge")
}

mixin {
    config("${mod.id}.mixins.json")
    config("${mod.id}.forge.mixins.json")
}

setupCommon("forge")
setupCommonModDev("forge")

val shadedDep by configurations.named("shadedDep")

dependencies {
    api(libs.mixinextras.forge)
    annotationProcessor(libs.mixinextras.forge)
    jarJar(libs.mixinextras.forge)
    moddedApi("dev.nyon:KotlinLangForge:${libs.versions.kotlinlangforge.get()}-${klfLangVersion}+forge")

    if (stonecutter.eval(stonecutter.current.version, "<=1.20.4")) {
        api(jarJar("xyz.bluspring.sunset:sunset-config:${libs.versions.sunset.get()}:dfu6-merged") {
            isTransitive = false
        })
    } else {
        api(jarJar("xyz.bluspring.sunset:sunset-config:${libs.versions.sunset.get()}:dfu8-merged") {
            isTransitive = false
        })
    }

    moddedApi("xyz.bluspring.modernnetworking:modernnetworking-forge:${libs.versions.modernnetworking.get()}+${mod.dep("modernnetworking_mc", common?.project?.mod?.dep("modernnetworking_mc"))}")
}
