package com.cerbon.talk_balloons.config;

import java.util.Optional;

import com.cerbon.talk_balloons.TalkBalloons;
import xyz.bluspring.modernnetworking.api.v2.codec.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodecs;
import xyz.bluspring.modernnetworking.minecraft.api.v2.codec.MinecraftNetworkCodecs;

import net.minecraft.network.FriendlyByteBuf;
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
    public static final NetworkCodec<FriendlyByteBuf, SynchronizedConfigData> NETWORK_CODEC = CompositeCodecs.composite(
        NetworkCodecs.optional(MinecraftNetworkCodecs.IDENTIFIER), SynchronizedConfigData::balloonStyle,
        NetworkCodecs.optional(NetworkCodecs.VAR_INT), SynchronizedConfigData::textColor,
        NetworkCodecs.optional(NetworkCodecs.VAR_INT), SynchronizedConfigData::balloonTint,
        NetworkCodecs.optional(NetworkCodecs.VAR_INT), SynchronizedConfigData::balloonPadding,
        NetworkCodecs.optional(NetworkCodecs.BOOL), SynchronizedConfigData::onlyDisplayBalloons,
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
