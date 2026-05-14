package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;

import net.minecraft.server.level.ServerPlayer;

public class TBServerPacketHandler {
    public static void handleStatus(ServerPlayer player, TalkBalloonsStatusPacket packet) {
        if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
            TalkBalloons.addSupportedPlayer(player.getUUID());

            for (ServerPlayer p : player.getServer().getPlayerList().getPlayers()) {
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
        for (ServerPlayer p : player.getServer().getPlayerList().getPlayers()) {
            if (TalkBalloons.playerHasSupport(p.getUUID())) {
                VanillaPacketSender.sendToPlayer(p, newPacket);
            }
        }
    }
}
