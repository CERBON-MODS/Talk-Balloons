package com.cerbon.talk_balloons.config

import com.cerbon.talk_balloons.client.resources.BalloonStyles
import java.util.EnumSet
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

object TBConfig : ITBConfig {
    override var balloonsHeightOffset: Float = 0.4f
    override var distanceBetweenBalloons: Int = 3

    override var maxBalloons: Int = 7

    override var minBalloonWidth: Int = 8
    override var maxBalloonWidth: Int = 180

    override var balloonPadding: Int = 2
    override var balloonAge: Int = 15
    override var balloonStyle: ITBConfig.IdentifierHolder = BalloonStyles.ROUNDED.holder

    override var textColor: Int = 0x141414 // RGB-encoded
    override var balloonTint: Int = 0xF1F6F8 // RGB-encoded

    override var balloonOpacity: Float = 0.95f
    override var balloonSneakingOpacity: Float = 0.65f

    override var showOwnBalloon: Boolean = true
    override var onlyDisplayBalloons: Boolean = false

    override var syncedConfigs: EnumSet<SynchronizedConfigType> = EnumSet.allOf(SynchronizedConfigType::class.java)
}
