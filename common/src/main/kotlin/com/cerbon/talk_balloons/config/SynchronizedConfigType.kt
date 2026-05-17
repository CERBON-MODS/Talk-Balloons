package com.cerbon.talk_balloons.config

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable
import java.util.EnumSet

enum class SynchronizedConfigType(private val serialized: String) : StringRepresentable {
    BALLOON_STYLE("balloon_style"),
    TEXT_COLOR("text_color"), BALLOON_TINT("balloon_tint"), BALLOON_PADDING("balloon_padding"),
    ONLY_DISPLAY_BALLOONS("only_display_balloons"),
    ;

    override fun getSerializedName(): String {
        return this.serialized
    }

    companion object {
        @JvmField val CODEC: Codec<SynchronizedConfigType> = StringRepresentable.fromValues(SynchronizedConfigType::values)
        @JvmField val SET_CODEC: Codec<EnumSet<SynchronizedConfigType>> = CODEC.listOf().xmap(EnumSet<SynchronizedConfigType>::copyOf, EnumSet<SynchronizedConfigType>::toList)
    }
}
