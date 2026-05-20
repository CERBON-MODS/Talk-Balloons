package com.cerbon.talk_balloons.config

import com.cerbon.talk_balloons.TalkBalloons
import com.cerbon.talk_balloons.config.ITBConfig.IdentifierHolder
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import xyz.bluspring.sunset.SunsetConfig
import xyz.bluspring.sunset.serializer.JsonWithCommentsSerializer
import kotlin.io.path.Path

object TBConfigManager {
    val path = Path("config/talk_balloons.json5")
    val config = SunsetConfig.create(path, JsonWithCommentsSerializer()) {
        float("balloonsHeightOffset", TBConfig::balloonsHeightOffset)
        integer("distanceBetweenBalloons", TBConfig::distanceBetweenBalloons)
        integer("maxBalloons", 1, 16, TBConfig::maxBalloons)
        integer("minBalloonWidth", 8, 512, TBConfig::minBalloonWidth)
        integer("maxBalloonWidth", 8, 512, TBConfig::maxBalloonWidth)
        integer("balloonPadding", 0, 64, TBConfig::balloonPadding)
        integer("balloonAge", 0, 120, TBConfig::balloonAge)
            .comment("In seconds")

        value("balloonOpacity", Codec.withAlternative(
            Codec.floatRange(0.15f, 1f),
            Codec.INT.xmap({ it / 255f }, { (it * 255).toInt() })
        ), TBConfig::balloonOpacity)
        value("balloonSneakingOpacity", Codec.withAlternative(
            Codec.floatRange(0.15f, 1f),
            Codec.INT.xmap({ it / 255f }, { (it * 255).toInt() })
        ), TBConfig::balloonSneakingOpacity)

        value("balloonStyle", Codec.withAlternative(
            // convert old balloon style to new variant
            Codec.STRING.comapFlatMap({ oldId -> if (oldId.contains(":")) DataResult.error { "This is an actual ID!" } else DataResult.success(TalkBalloons.id("classic/${oldId.lowercase()}")) }, Identifier::toString),
            Identifier.CODEC
        ).xmap(::IdentifierHolder, IdentifierHolder::identifier), TBConfig::balloonStyle)
        integer("textColor", TBConfig::textColor)
        integer("balloonTint", TBConfig::balloonTint)
        value("showOwnBalloon", Codec.BOOL, TBConfig::showOwnBalloon)
        value("onlyDisplayBalloons", Codec.BOOL, TBConfig::onlyDisplayBalloons)

        float("balloonFadeOut", 0f, 5f, TBConfig::balloonFadeOut)

        value("syncedConfigs", SynchronizedConfigType.SET_CODEC, TBConfig::syncedConfigs)
    }
}
