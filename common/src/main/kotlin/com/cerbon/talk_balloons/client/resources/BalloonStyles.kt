package com.cerbon.talk_balloons.client.resources

//? if < 1.21.11 {
import com.cerbon.talk_balloons.TalkBalloons
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

object BalloonStyles {
    @JvmField val CIRCULAR: Identifier = TalkBalloons.id("circular")
    @JvmField val ROUNDED: Identifier = TalkBalloons.id("rounded")
    @JvmField val ROUNDED_1PX: Identifier = TalkBalloons.id("rounded_1px")
    @JvmField val SQUARED: Identifier = TalkBalloons.id("squared")
}
