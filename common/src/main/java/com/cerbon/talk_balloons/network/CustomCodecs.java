package com.cerbon.talk_balloons.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.FriendlyByteBuf;
//? if < 1.21.11 {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
 //?} else {
/*import net.minecraft.resources.Identifier;
*///?}

public class CustomCodecs {
    public static <B extends FriendlyByteBuf, E extends Enum<E>> StreamCodec<B, E> enumCodec(Class<E> enumClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(enumClass));
    }
}
