package com.cerbon.talk_balloons.config

import com.cerbon.talk_balloons.TalkBalloons
import com.mojang.serialization.Codec
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import xyz.bluspring.sunset.SunsetConfig
import kotlin.io.path.Path

object TBConfigManager {
    val path = Path("config/talk_balloons.json5")
    val config = SunsetConfig.create(path) {
        float("balloonsHeightOffset", TBConfig::balloonsHeightOffset)
        integer("distanceBetweenBalloons", TBConfig::distanceBetweenBalloons)
        integer("maxBalloons", 1, 16, TBConfig::maxBalloons)
        integer("minBalloonWidth", 0, 512, TBConfig::minBalloonWidth)
        integer("maxBalloonWidth", 0, 512, TBConfig::maxBalloonWidth)
        integer("balloonPadding", 0, 64, TBConfig::balloonPadding)
        integer("balloonAge", 0, 120, TBConfig::balloonAge)
            .comment("In seconds")
        value("balloonStyle", Codec.withAlternative(
            Identifier.CODEC,
            // convert old balloon style to new variant
            Codec.STRING.xmap({ oldId -> TalkBalloons.id(oldId.lowercase()) }, Identifier::toString)
        ), TBConfig::balloonStyle)
        integer("textColor", TBConfig::textColor)
        integer("balloonTint", TBConfig::balloonTint)
        value("showOwnBalloon", Codec.BOOL, TBConfig::showOwnBalloon)
        value("onlyDisplayBalloons", Codec.BOOL, TBConfig::onlyDisplayBalloons)
    }
}
