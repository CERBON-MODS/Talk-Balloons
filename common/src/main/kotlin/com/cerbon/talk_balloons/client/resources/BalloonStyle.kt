package com.cerbon.talk_balloons.client.resources


import com.cerbon.talk_balloons.TalkBalloons
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//? } else {
/*import net.minecraft.resources.Identifier
*///? }

@JvmRecord
data class BalloonStyle(
    val balloon: Identifier,
    val arrow: Identifier,
    val allowsTint: Boolean,
    val padding: Padding,
) {
    data class Padding(
        val top: Int,
        val bottom: Int,
        val left: Int,
        val right: Int
    ) {
        constructor(padding: Int) : this(padding, padding, padding, padding)

        companion object {
            @JvmField
            val CODEC: Codec<Padding> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.optionalFieldOf("top", 1)
                        .forGetter(Padding::top),
                    Codec.INT.optionalFieldOf("bottom", 1)
                        .forGetter(Padding::bottom),
                    Codec.INT.optionalFieldOf("left", 1)
                        .forGetter(Padding::left),
                    Codec.INT.optionalFieldOf("right", 1)
                        .forGetter(Padding::right)
                )
                    .apply(instance, ::Padding)
            }
        }
    }

    companion object {
        @JvmField val BALLOONS_SHEET: Identifier = TalkBalloons.id("textures/atlas/balloons.png")
        @JvmField val BALLOONS_ATLAS: Identifier = TalkBalloons.id("balloons")

        @JvmField val DEFAULT_ARROW: Identifier = TalkBalloons.id("arrow")
        @JvmField val FALLBACK = BalloonStyle(BalloonStyles.ROUNDED, DEFAULT_ARROW, true, Padding(1))

        @JvmField
        val CODEC: Codec<BalloonStyle> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("balloon")
                    .forGetter(BalloonStyle::balloon),
                Identifier.CODEC.optionalFieldOf("arrow", DEFAULT_ARROW)
                    .forGetter(BalloonStyle::arrow),
                Codec.BOOL.optionalFieldOf("allows_tint", false)
                    .forGetter(BalloonStyle::allowsTint),
                Codec.withAlternative(
                    Padding.CODEC,
                    Codec.withAlternative(
                        Codec.INT.listOf(4, 4).xmap({ Padding(it[0], it[1], it[2], it[3]) }, { listOf(it.top, it.bottom, it.left, it.right) }),
                        Codec.INT.xmap(::Padding, Padding::top)
                    )
                ).optionalFieldOf("padding", Padding(1))
                    .forGetter(BalloonStyle::padding)
            )
                .apply(instance, ::BalloonStyle)
        }
    }
}
