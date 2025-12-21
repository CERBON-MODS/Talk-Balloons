package com.cerbon.talk_balloons.network;

import net.minecraft.network.FriendlyByteBuf;
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation;
 //?} else {
/*import net.minecraft.resources.Identifier;
*///?}
import xyz.bluspring.modernnetworking.api.NetworkCodec;

public class CustomCodecs {
    public static final NetworkCodec</*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/, FriendlyByteBuf> RESOURCE_LOCATION = new NetworkCodec<>((buf, location) -> {
        //? if < 1.21.11 {
        buf.writeResourceLocation(location);
        //?} else {
        /*buf.writeIdentifier(location);
        *///?}
    },
        //? if < 1.21.11 {
        FriendlyByteBuf::readResourceLocation
        //?} else {
        /*FriendlyByteBuf::readIdentifier
        *///?}
    );
}
