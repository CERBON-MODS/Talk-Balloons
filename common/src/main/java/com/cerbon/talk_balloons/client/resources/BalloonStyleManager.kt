package com.cerbon.talk_balloons.client.resources

import com.cerbon.talk_balloons.TalkBalloons
import com.cerbon.talk_balloons.config.TBConfig
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.minecraft.resources.FileToIdConverter
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BalloonStyleManager : SimplePreparableReloadListener<Map<Identifier, BalloonStyle>>() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BalloonStyleManager::class.java)
        private val styles: MutableMap<Identifier, BalloonStyle> = mutableMapOf()

        @JvmStatic
        fun getStyleById(id: Identifier): BalloonStyle {
            return this.styles[id] ?: this.styles[TBConfig.balloonStyle] ?: this.styles[BalloonStyles.ROUNDED]
                ?: BalloonStyle.FALLBACK
        }

        @JvmStatic
        fun getStyleId(style: BalloonStyle): Identifier {
            return this.styles.filterValues { it == style }.keys.firstOrNull()
                ?: BalloonStyles.ROUNDED
        }
    }

    override fun prepare(manager: ResourceManager, profiler: ProfilerFiller): Map<Identifier, BalloonStyle> {
        val map = mutableMapOf<Identifier, BalloonStyle>()

        val converter = FileToIdConverter.json("talk_balloons/styles")

        for ((fileId, resource) in converter.listMatchingResources(manager)) {
            val id = converter.fileToId(fileId)

            try {
                resource.openAsReader().use { reader ->
                    val json = JsonParser.parseReader(reader)
                    map[id] = BalloonStyle.CODEC.decode(JsonOps.INSTANCE, json).orThrow.first
                }
            } catch (e: Throwable) {
                logger.error("Failed to parse balloon style $id from $fileId", e)
            }
        }

        return map
    }

    override fun apply(styles: Map<Identifier, BalloonStyle>, manager: ResourceManager, profiler: ProfilerFiller) {
        synchronized(BalloonStyleManager.styles) {
            BalloonStyleManager.styles.clear()
            BalloonStyleManager.styles.putAll(styles)
        }
    }
}
