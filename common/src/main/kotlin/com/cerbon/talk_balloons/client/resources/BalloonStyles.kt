package com.cerbon.talk_balloons.client.resources

import com.cerbon.talk_balloons.TalkBalloons
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

object BalloonStyles {
    @JvmField val CIRCULAR: Identifier = TalkBalloons.id("classic/circular")
    @JvmField val ROUNDED: Identifier = TalkBalloons.id("classic/rounded")
    @JvmField val ROUNDED_1PX: Identifier = TalkBalloons.id("classic/rounded_1px")
    @JvmField val SQUARED: Identifier = TalkBalloons.id("classic/squared")
}
