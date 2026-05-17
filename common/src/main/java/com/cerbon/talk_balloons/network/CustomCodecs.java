package com.cerbon.talk_balloons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CustomCodecs {
    public static <B extends FriendlyByteBuf, E extends Enum<E>> StreamCodec<B, E> enumCodec(Class<E> enumClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(enumClass));
    }
}
