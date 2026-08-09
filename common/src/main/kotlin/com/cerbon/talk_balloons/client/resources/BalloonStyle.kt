package com.cerbon.talk_balloons.client.resources

import com.cerbon.talk_balloons.TalkBalloons
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
//? if <= 1.20.4 {
import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import java.util.function.Function
//? }
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
    val margins: Margins,
) {
    @JvmRecord
    data class Margins(
        val top: Int,
        val bottom: Int,
        val left: Int,
        val right: Int
    ) {
        constructor(padding: Int) : this(padding, padding, padding, padding)

        val horizontalMargins: Int
            get() = left + right

        val verticalMargins: Int
            get() = top + bottom

        companion object {
            @JvmField
            val CODEC: Codec<Margins> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.optionalFieldOf("top", 1)
                        .forGetter(Margins::top),
                    Codec.INT.optionalFieldOf("bottom", 1)
                        .forGetter(Margins::bottom),
                    Codec.INT.optionalFieldOf("left", 1)
                        .forGetter(Margins::left),
                    Codec.INT.optionalFieldOf("right", 1)
                        .forGetter(Margins::right)
                )
                    .apply(instance, ::Margins)
            }
        }
    }

    companion object {
        @JvmField val BALLOONS_SHEET: Identifier = TalkBalloons.id("textures/atlas/balloons.png")
        @JvmField val BALLOONS_ATLAS: Identifier = TalkBalloons.id("balloons")

        @JvmField val DEFAULT_ARROW: Identifier = TalkBalloons.id("arrow")
        @JvmField val FALLBACK = BalloonStyle(BalloonStyles.ROUNDED, DEFAULT_ARROW, true, Margins(1))

        @JvmField
        val CODEC: Codec<BalloonStyle> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("balloon")
                    .forGetter(BalloonStyle::balloon),
                Identifier.CODEC.optionalFieldOf("arrow", DEFAULT_ARROW)
                    .forGetter(BalloonStyle::arrow),
                Codec.BOOL.optionalFieldOf("allows_tint", false)
                    .forGetter(BalloonStyle::allowsTint),
                withAlternative(
                    Margins.CODEC,
                    withAlternative(
                        Codec.INT.listOf(4, 4).xmap({ Margins(it[0], it[1], it[2], it[3]) }, { listOf(it.top, it.bottom, it.left, it.right) }),
                        Codec.INT.xmap(::Margins, Margins::top)
                    )
                ).optionalFieldOf("margin", Margins(1))
                    .forGetter(BalloonStyle::margins)
            )
                .apply(instance, ::BalloonStyle)
        }

        //? if <= 1.20.4 {
        private fun <T> withAlternative(primary: Codec<T>, alternative: Codec<out T>): Codec<T> {
            return Codec.either(primary, alternative)
                .xmap({ it.map(Function.identity(), Function.identity()) }, { Either.left(it) })
        }

        private fun <T> Codec<T>.listOf(min: Int, max: Int): Codec<List<T>> = this.listOf()
            .flatXmap({
                if (it.size !in min..max)
                    DataResult.error { "List is not between [$min,$max]!" }
                else
                    DataResult.success(it)
            }, {
                if (it.size !in min..max)
                    DataResult.error { "List is not between [$min,$max]!" }
                else
                    DataResult.success(it)
            })
        //? } else {
        /*private fun <T> withAlternative(primary: Codec<T>, alternative: Codec<out T>): Codec<T>
            = Codec.withAlternative(primary, alternative)
        *///? }
    }
}
