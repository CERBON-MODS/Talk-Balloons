plugins {
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("dev.kikugie.j52j") version "2.0"
}

val minecraftVersion = stonecutter.current.version

version = "${mod.version}+$minecraftVersion"
base {
    archivesName.set("${mod.id}-common")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered() {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$minecraftVersion:${mod.prop("parchment_snapshot")}")
    })

    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    modImplementation("me.shedaniel.cloth:cloth-config:${mod.dep("cloth_config")}")

    implementation("xyz.bluspring.modernnetworking:modernnetworking-api:${mod.dep("modernnetworking")}")
    modImplementation("xyz.bluspring.modernnetworking:modernnetworking-common:${mod.dep("modernnetworking")}+${mod.dep("modernnetworking_mc")}")
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraftVersion, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}