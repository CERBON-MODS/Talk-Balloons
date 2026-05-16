package com.cerbon.talk_balloons.util;

import com.cerbon.talk_balloons.TalkBalloons;
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
    /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ balloonStyle,
    int textColor, int balloonTint,
    int balloonPadding,
    boolean onlyDisplayBalloons
) {
    public static final Codec<SynchronizedConfigData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/.CODEC
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
        /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/.STREAM_CODEC, SynchronizedConfigData::balloonStyle,
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
