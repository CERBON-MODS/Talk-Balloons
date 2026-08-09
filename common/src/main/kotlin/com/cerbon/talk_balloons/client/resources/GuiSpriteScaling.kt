package com.cerbon.talk_balloons.client.resources

//? if <= 1.20.1 {
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.StringRepresentable
import java.util.OptionalInt
import java.util.function.Function

sealed interface GuiSpriteScaling {
    companion object {
        @JvmField val CODEC: Codec<GuiSpriteScaling> = Type.CODEC.dispatch("type", GuiSpriteScaling::type, Type::codec)
    }

    val type: Type

    data class NineSlice(val width: Int, val height: Int, val border: Border, val stretchInner: Boolean) : GuiSpriteScaling {
        override val type: Type
            get() = Type.NINE_SLICE

        fun width(): Int = this.width
        fun height(): Int = this.height

        @JvmRecord
        data class Border(
            val left: Int, val top: Int,
            val right: Int, val bottom: Int,
        ) {
            private fun unpackValue(): OptionalInt = if (this.left == this.top && this.top == this.right && this.right == this.bottom)
                OptionalInt.of(this.left)
            else OptionalInt.empty()

            companion object {
                private val VALUE_CODEC: Codec<Border> = ExtraCodecs.POSITIVE_INT
                    .flatComapMap({ size -> Border(size, size, size, size) }, { border ->
                        val size = border.unpackValue()
                        if (size.isPresent)
                            DataResult.success(size.asInt)
                        else
                            DataResult.error { "Border has different size sides" }
                    })
                private val RECORD_CODEC: Codec<Border> = RecordCodecBuilder.create { instance ->
                    instance.group(
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("left")
                            .forGetter(Border::left),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("top")
                            .forGetter(Border::top),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("right")
                            .forGetter(Border::right),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bottom")
                            .forGetter(Border::bottom),
                    )
                        .apply(instance, ::Border)
                }

                val CODEC: Codec<Border> = Codec.either(VALUE_CODEC, RECORD_CODEC)
                    .xmap({ it.map(Function.identity(), Function.identity()) }) { border ->
                        if (border.unpackValue().isPresent)
                            Either.left(border)
                        else
                            Either.right(border)
                    }
            }
        }

        companion object {
            @JvmField val CODEC: Codec<NineSlice> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("width")
                        .forGetter { it.width },
                    ExtraCodecs.POSITIVE_INT.fieldOf("height")
                        .forGetter { it.height },
                    Border.CODEC.fieldOf("border")
                        .forGetter(NineSlice::border),
                    Codec.BOOL.optionalFieldOf("stretch_inner", false)
                        .forGetter(NineSlice::stretchInner),
                )
                    .apply(instance, ::NineSlice)
            }
        }
    }

    object Stretch : GuiSpriteScaling {
        @JvmField val CODEC: Codec<Stretch> = Codec.unit(Stretch)

        override val type: Type
            get() = Type.STRETCH
    }

    @JvmRecord
    data class Tile(val width: Int, val height: Int) : GuiSpriteScaling {
        companion object {
            @JvmField val CODEC: Codec<Tile> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("width")
                        .forGetter(Tile::width),
                    ExtraCodecs.POSITIVE_INT.fieldOf("height")
                        .forGetter(Tile::height),
                )
                    .apply(instance, ::Tile)
            }
        }

        override val type: Type
            get() = Type.TILE
    }

    enum class Type(private val key: String, val codec: Codec<out GuiSpriteScaling>) : StringRepresentable {
        STRETCH("stretch", Stretch.CODEC),
        TILE("tile", Tile.CODEC),
        NINE_SLICE("nine_slice", NineSlice.CODEC),
        ;

        override fun getSerializedName(): String = this.key

        companion object {
            @JvmField val CODEC: Codec<Type> = StringRepresentable.fromEnum(Type::values)
        }
    }
}
//? }
