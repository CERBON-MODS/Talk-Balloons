package com.cerbon.talk_balloons.network;

import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.minecraft.api.v2.packet.client.MinecraftClientPacketHandlers;

public class VanillaClientPacketSender {
    public static void sendToServer(NetworkPacket payload) {
        MinecraftClientPacketHandlers.PLAY.send(payload);
    }
}
