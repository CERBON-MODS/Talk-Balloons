plugins {
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.shadow)
}

val mcVersion = stonecutter.current.version
val common = stonecutter.node.sibling("")!!

neoForge {
    version = mod.dep("neoforge", common.project.mod.dep("neoforge")) as String

    configureModDev(this, "neoforge")
}

setupCommon("neoforge")
setupCommonModDev("neoforge")

val shadedDep by configurations.named("shadedDep")

dependencies {
    api("dev.nyon:KotlinLangForge:${libs.versions.kotlinlangforge.get()}-${klfLangVersion}+neoforge")
    api(libs.sunset)
    jarJar(libs.sunset)
    jarJar(api("dev.isxander:yet-another-config-lib:${mod.dep("yacl", common?.project?.mod?.dep("yacl"))}-neoforge")!!)
}
