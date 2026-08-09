package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusServerPacket;
import xyz.bluspring.modernnetworking.minecraft.api.v2.packet.MinecraftServerPacketHandlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class TBServerPacketHandler {
    public static void init() {
        MinecraftServerPacketHandlers.PLAY.register(TBPackets.STATUS_SERVER, (packet, ctx) -> handleStatus(ctx.getPlayer(), packet));
        MinecraftServerPacketHandlers.PLAY.register(TBPackets.SYNC_BALLOON_CONFIG, (packet, ctx) -> handleSyncBalloonConfig(ctx.getPlayer(), packet));
    }

    public static void handleStatus(ServerPlayer player, TalkBalloonsStatusServerPacket packet) {
        if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
            TalkBalloons.addSupportedPlayer(player.getUUID());

            //? if < 1.21.9 {
            MinecraftServer server = player.getServer();
            //? } else {
            /*MinecraftServer server = player.level().getServer();
            *///? }

            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                var config = TalkBalloons.serverSyncedConfigs.getSetPlayerConfig(p.getUUID());

                if (config != null) {
                    VanillaPacketSender.sendToPlayer(player, new SyncBalloonConfigToPlayerPacket(p.getUUID(), config));
                }
            }
        }
    }

    public static void handleSyncBalloonConfig(ServerPlayer player, SyncBalloonConfigPacket packet) {
        TalkBalloons.serverSyncedConfigs.setPlayerConfig(player.getUUID(), packet.data());

        var newPacket = new SyncBalloonConfigToPlayerPacket(player.getUUID(), packet.data());
        //? if < 1.21.9 {
        MinecraftServer server = player.getServer();
         //? } else {
        /*MinecraftServer server = player.level().getServer();
        *///? }

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (TalkBalloons.playerHasSupport(p.getUUID())) {
                VanillaPacketSender.sendToPlayer(p, newPacket);
            }
        }
    }
}
