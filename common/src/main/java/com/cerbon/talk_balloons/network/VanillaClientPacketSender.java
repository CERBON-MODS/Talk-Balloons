package com.cerbon.talk_balloons.network;

import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.minecraft.api.v2.packet.client.MinecraftClientPacketHandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class VanillaClientPacketSender {
    public static void sendToServer(NetworkPacket payload) {
        MinecraftClientPacketHandlers.PLAY.send(payload);
    }
}
