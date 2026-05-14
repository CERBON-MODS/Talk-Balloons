package com.cerbon.talk_balloons.util;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.config.BalloonStyle;
import com.cerbon.talk_balloons.network.CustomCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SynchronizedConfigData(
    BalloonStyle balloonStyle,
    int textColor, int balloonTint,
    int balloonPadding,
    boolean onlyDisplayBalloons
) {
    public static final Codec<SynchronizedConfigData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BalloonStyle.CODEC
                .optionalFieldOf("balloonStyle", TalkBalloons.config.getBalloonStyle())
                .forGetter(SynchronizedConfigData::balloonStyle),
            Codec.INT
                .optionalFieldOf("textColor", TalkBalloons.config.getTextColor())
                .forGetter(SynchronizedConfigData::textColor),
            Codec.INT
                .optionalFieldOf("balloonTint", TalkBalloons.config.getBalloonTint())
                .forGetter(SynchronizedConfigData::balloonTint),
            Codec.INT
                .optionalFieldOf("balloonPadding", TalkBalloons.config.getBalloonPadding())
                .forGetter(SynchronizedConfigData::balloonPadding),
            Codec.BOOL
                .optionalFieldOf("onlyDisplayBalloons", TalkBalloons.config.getOnlyDisplayBalloons())
                .forGetter(SynchronizedConfigData::onlyDisplayBalloons)
        )
            .apply(instance, SynchronizedConfigData::new)
    );

    public static final StreamCodec<FriendlyByteBuf, SynchronizedConfigData> NETWORK_CODEC = StreamCodec.composite(
        BalloonStyle.NETWORK_CODEC, SynchronizedConfigData::balloonStyle,
        ByteBufCodecs.VAR_INT, SynchronizedConfigData::textColor,
        ByteBufCodecs.VAR_INT, SynchronizedConfigData::balloonTint,
        ByteBufCodecs.VAR_INT, SynchronizedConfigData::balloonPadding,
        ByteBufCodecs.BOOL, SynchronizedConfigData::onlyDisplayBalloons,
        SynchronizedConfigData::new
    );

    public static SynchronizedConfigData getDefault() {
        return new SynchronizedConfigData(
            TalkBalloons.config.getBalloonStyle(),
            TalkBalloons.config.getTextColor(),
            TalkBalloons.config.getBalloonTint(),
            TalkBalloons.config.getBalloonPadding(),
            TalkBalloons.config.getOnlyDisplayBalloons()
        );
    }
}
