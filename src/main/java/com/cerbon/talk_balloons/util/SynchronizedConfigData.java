package com.cerbon.talk_balloons.util;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.config.BalloonStyle;
import com.cerbon.talk_balloons.network.CustomCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import xyz.bluspring.modernnetworking.api.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.NetworkCodec;
import xyz.bluspring.modernnetworking.api.NetworkCodecs;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaCodecs;

public record SynchronizedConfigData(
    BalloonStyle balloonStyle,
    int textColor, int balloonPadding
) {
    public static final Codec<SynchronizedConfigData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BalloonStyle.CODEC
                .optionalFieldOf("balloonStyle", TalkBalloons.config.balloonStyle)
                .forGetter(SynchronizedConfigData::balloonStyle),
            Codec.INT
                .optionalFieldOf("textColor", TalkBalloons.config.textColor)
                .forGetter(SynchronizedConfigData::textColor),
            Codec.INT
                .optionalFieldOf("balloonPadding", TalkBalloons.config.balloonPadding)
                .forGetter(SynchronizedConfigData::balloonPadding)
        )
            .apply(instance, SynchronizedConfigData::new)
    );

    public static final NetworkCodec<SynchronizedConfigData, FriendlyByteBuf> NETWORK_CODEC = CompositeCodecs.composite(
        BalloonStyle.NETWORK_CODEC, SynchronizedConfigData::balloonStyle,
        NetworkCodecs.VAR_INT, SynchronizedConfigData::textColor,
        NetworkCodecs.VAR_INT, SynchronizedConfigData::balloonPadding,
        SynchronizedConfigData::new
    );
}
