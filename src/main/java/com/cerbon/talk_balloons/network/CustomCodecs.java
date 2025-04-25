package com.cerbon.talk_balloons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import xyz.bluspring.modernnetworking.api.NetworkCodec;

public class CustomCodecs {
    public static final NetworkCodec<ResourceLocation, FriendlyByteBuf> RESOURCE_LOCATION = new NetworkCodec<>((buf, location) -> {
        buf.writeResourceLocation(location);
    }, FriendlyByteBuf::readResourceLocation);
}
