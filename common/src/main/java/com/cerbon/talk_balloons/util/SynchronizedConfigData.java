package com.cerbon.talk_balloons.util;

import java.util.Optional;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.config.ITBConfig;
import com.cerbon.talk_balloons.config.SynchronizedConfigType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation;
    //?} else {
/*import net.minecraft.resources.Identifier;
 *///?}

public record SynchronizedConfigData(
    Optional</*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/> balloonStyle,
    Optional<Integer> textColor, Optional<Integer> balloonTint,
    Optional<Integer> balloonPadding,
    Optional<Boolean> onlyDisplayBalloons
) {
    public static final SynchronizedConfigData EMPTY = new SynchronizedConfigData(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final StreamCodec<FriendlyByteBuf, SynchronizedConfigData> NETWORK_CODEC = StreamCodec.composite(
        ByteBufCodecs.optional(/*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/.STREAM_CODEC), SynchronizedConfigData::balloonStyle,
        ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), SynchronizedConfigData::textColor,
        ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), SynchronizedConfigData::balloonTint,
        ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), SynchronizedConfigData::balloonPadding,
        ByteBufCodecs.optional(ByteBufCodecs.BOOL), SynchronizedConfigData::onlyDisplayBalloons,
        SynchronizedConfigData::new
    );
    
    public static SynchronizedConfigData getDefault() {
        return getDefault(TalkBalloons.config);
    }

    public static SynchronizedConfigData getDefault(ITBConfig config) {
        var syncedConfigs = config.getSyncedConfigs();

        return new SynchronizedConfigData(
            syncedConfigs.contains(SynchronizedConfigType.BALLOON_STYLE) ? Optional.of(config.getBalloonStyle().identifier()) : Optional.empty(),
            syncedConfigs.contains(SynchronizedConfigType.TEXT_COLOR) ? Optional.of(config.getTextColor()) : Optional.empty(),
            syncedConfigs.contains(SynchronizedConfigType.BALLOON_TINT) ? Optional.of(config.getBalloonTint()) : Optional.empty(),
            syncedConfigs.contains(SynchronizedConfigType.BALLOON_PADDING) ? Optional.of(config.getBalloonPadding()) : Optional.empty(),
            syncedConfigs.contains(SynchronizedConfigType.ONLY_DISPLAY_BALLOONS) ? Optional.of(config.getOnlyDisplayBalloons()) : Optional.empty()
        );
    }
}
