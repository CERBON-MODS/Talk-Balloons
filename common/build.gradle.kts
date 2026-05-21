import net.neoforged.moddevgradle.dsl.NeoForgeExtension
import net.neoforged.moddevgradle.legacyforge.dsl.LegacyForgeExtension

plugins {
    alias(libs.plugins.fletching.table)
    `maven-publish`
}

val mcVersion = stonecutter.current.version
val common = stonecutter.node.sibling("")!!
val isLegacy = stonecutter.eval(mcVersion, "<=1.20.1")

if (isLegacy) {
    apply(plugin = "net.neoforged.moddev.legacyforge")

    project.extensions.configure<LegacyForgeExtension> {
        mcpVersion = mcVersion

        configureModDev(this, "common")
    }
} else {
    apply(plugin = "net.neoforged.moddev")

    project.extensions.configure<NeoForgeExtension> {
        neoFormVersion = tryFindNeoFormVersion(mcVersion)!!

        configureModDev(this, "common")
    }
}

setupCommon("common")
setupCommonModDev("common")

dependencies {
    api(libs.mixin) // Mixin
    api(libs.fabric.kotlin) // Provides all the Kotlin stuff we'd ever need
//    annotationProcessor(libs.mixinextras.common) // MixinExtras
    api(libs.mixinextras.common)
    api(libs.sunset)
    compileOnly(libs.iris)
    compileOnly("dev.isxander:yet-another-config-lib:${mod.dep("yacl", common?.project?.mod?.dep("yacl"))}-${if (isLegacy) "forge" else "neoforge"}") // Use the Neo variant, since this is MDG.
}
