package com.cerbon.talk_balloons.network;

import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.minecraft.api.v2.packet.MinecraftServerPacketHandlers;

import net.minecraft.server.level.ServerPlayer;

public class VanillaPacketSender {
    public static void sendToPlayer(ServerPlayer player, NetworkPacket payload) {
        MinecraftServerPacketHandlers.PLAY.send(player, payload);
    }
}
