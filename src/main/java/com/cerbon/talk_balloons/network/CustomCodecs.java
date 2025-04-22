package com.cerbon.talk_balloons.network;

import io.netty.buffer.ByteBuf;
import kotlin.Unit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import xyz.bluspring.modernnetworking.api.NetworkCodec;

public class CustomCodecs {
    public static final NetworkCodec<ResourceLocation, FriendlyByteBuf> RESOURCE_LOCATION = new NetworkCodec<>((buf, location) -> {
        buf.writeResourceLocation(location);
        return Unit.INSTANCE;
    }, FriendlyByteBuf::readResourceLocation);

    public static <T, B extends ByteBuf> NetworkCodec<T, B> createNullable(NetworkCodec<T, B> original) {
        return new NetworkCodec<>((buf, value) -> {
            buf.writeBoolean(value != null);
            if (value != null) {
                original.encode(buf, value);
            }

            return Unit.INSTANCE;
        }, (buf) -> {
            if (buf.readBoolean()) {
                return original.decode(buf);
            }

            return null;
        });
    }
}
