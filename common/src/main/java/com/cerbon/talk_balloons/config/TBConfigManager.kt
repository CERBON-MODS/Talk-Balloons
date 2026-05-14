package com.cerbon.talk_balloons.config

import com.mojang.serialization.Codec
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
        value("balloonStyle", BalloonStyle.CODEC, TBConfig::balloonStyle)
        integer("textColor", TBConfig::textColor)
        integer("balloonTint", TBConfig::balloonTint)
        value("showOwnBalloon", Codec.BOOL, TBConfig::showOwnBalloon)
        value("onlyDisplayBalloons", Codec.BOOL, TBConfig::onlyDisplayBalloons)
    }
}
