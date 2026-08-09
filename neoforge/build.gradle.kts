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

    api(jarJar("xyz.bluspring.sunset:sunset-config:${libs.versions.sunset.get()}") {
        isTransitive = false
    })
    if (stonecutter.eval(stonecutter.current.version, "<=1.20.4")) {
        api(jarJar("xyz.bluspring.sunset:sunset-config:${libs.versions.sunset.get()}:dfu6") {
            isTransitive = false
        })
    } else {
        api(jarJar("xyz.bluspring.sunset:sunset-config:${libs.versions.sunset.get()}:dfu8") {
            isTransitive = false
        })
    }

    jarJar(api("dev.isxander:yet-another-config-lib:${mod.dep("yacl", common?.project?.mod?.dep("yacl"))}-neoforge") {
        exclude(group = "thedarkcolour") // go away KFF
    })
    api("xyz.bluspring.modernnetworking:modernnetworking-neoforge:${libs.versions.modernnetworking.get()}+${mod.dep("modernnetworking_mc", common?.project?.mod?.dep("modernnetworking_mc"))}")
}
