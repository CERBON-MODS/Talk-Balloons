package com.cerbon.talk_balloons.config

import java.util.EnumSet
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

interface ITBConfig {
    val balloonsHeightOffset: Float
    val distanceBetweenBalloons: Int
    
    val maxBalloons: Int
    
    val minBalloonWidth: Int
    val maxBalloonWidth: Int
    
    val balloonPadding: Int
    val balloonAge: Int
    val balloonStyle: IdentifierHolder
    
    val textColor: Int
    val balloonTint: Int
    
    val balloonOpacity: Float
    val balloonSneakingOpacity: Float
    
    val showOwnBalloon: Boolean
    val onlyDisplayBalloons: Boolean
    
    val syncedConfigs: EnumSet<SynchronizedConfigType>

    // Workaround to https://github.com/FabricMC/tiny-remapper/issues/165
    @JvmRecord
    data class IdentifierHolder(val identifier: Identifier)

    val Identifier.holder: IdentifierHolder
        get() = IdentifierHolder(this)
}
