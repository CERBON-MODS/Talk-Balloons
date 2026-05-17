package com.cerbon.talk_balloons.config

import com.cerbon.talk_balloons.client.resources.BalloonStyles
import java.util.EnumSet
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

object TBConfig {
    var balloonsHeightOffset: Float = 0.4f
    var distanceBetweenBalloons: Int = 3

    var maxBalloons: Int = 7

    var minBalloonWidth: Int = 13
    var maxBalloonWidth: Int = 180

    var balloonPadding: Int = 2
    var balloonAge: Int = 15
    var balloonStyle: IdentifierHolder = BalloonStyles.ROUNDED.holder

    var textColor: Int = 0x141414 // RGB-encoded
    var balloonTint: Int = 0xF1F6F8 // RGB-encoded

    var balloonOpacity: Int = 240
    var balloonSneakingOpacity: Int = 165

    var showOwnBalloon: Boolean = true
    var onlyDisplayBalloons: Boolean = false

    var syncedConfigs: EnumSet<SynchronizedConfigType> = EnumSet.allOf(SynchronizedConfigType::class.java)

    // Workaround to https://github.com/FabricMC/tiny-remapper/issues/165
    @JvmRecord
    data class IdentifierHolder(val identifier: Identifier)

    val Identifier.holder: IdentifierHolder
        get() = IdentifierHolder(this)
}
