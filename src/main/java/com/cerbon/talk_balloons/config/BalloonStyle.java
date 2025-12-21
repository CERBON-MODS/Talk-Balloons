package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.TalkBalloons;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?} else {
/*import net.minecraft.resources.Identifier;
*///?}
import net.minecraft.util.StringRepresentable;
import xyz.bluspring.modernnetworking.api.NetworkCodec;
import xyz.bluspring.modernnetworking.api.NetworkCodecs;

public enum BalloonStyle implements StringRepresentable {
    SQUARED("squared", TalkBalloons.id("textures/gui/squared.png")),
    ROUNDED("rounded", TalkBalloons.id("textures/gui/rounded.png")),
    CIRCULAR("circular", TalkBalloons.id("textures/gui/circular.png"));
    // TODO: support custom balloons?

    public static final Codec<BalloonStyle> CODEC = StringRepresentable.fromEnum(BalloonStyle::values/*? if < 1.19 {*//*, BalloonStyle::valueOf*//*?}*/);
    public static final NetworkCodec<BalloonStyle, ByteBuf> NETWORK_CODEC = NetworkCodecs.enumCodec(BalloonStyle.class);

    private final String serializedName;
    private final /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ textureId;

    BalloonStyle(String serializedName, /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ textureId) {
        this.serializedName = serializedName;
        this.textureId = textureId;
    }

    public /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ getTextureId() {
        return this.textureId;
    }

    @Override
    public String getSerializedName() {
        return this.serializedName;
    }
}
