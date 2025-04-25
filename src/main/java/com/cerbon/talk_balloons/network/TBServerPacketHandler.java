package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

public class TBServerPacketHandler {
    public static void init() {
        var registry = TBPackets.REGISTRY;

        registry.addServerboundHandler(TBPackets.STATUS, (packet, ctx) -> {
            if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
                TalkBalloons.addSupportedPlayer(ctx.getPlayer().getUUID());

                for (ServerPlayer player : ctx.getServer().getPlayerList().getPlayers()) {
                    var config = TalkBalloons.serverSyncedConfigs.getSetPlayerConfig(player.getUUID());

                    if (config != null) {
                        VanillaPacketSender.sendToPlayer(ctx.getPlayer(), new SyncBalloonConfigToPlayerPacket(player.getUUID(), config));
                    }
                }
            }
        });

        registry.addServerboundHandler(TBPackets.SYNC_BALLOON_CONFIG, (packet, ctx) -> {
            TalkBalloons.serverSyncedConfigs.setPlayerConfig(ctx.getPlayer().getUUID(), packet.data());

            var newPacket = new SyncBalloonConfigToPlayerPacket(ctx.getPlayer().getUUID(), packet.data());
            for (ServerPlayer player : ctx.getServer().getPlayerList().getPlayers()) {
                if (TalkBalloons.playerHasSupport(player.getUUID())) {
                    VanillaPacketSender.sendToPlayer(player, newPacket);
                }
            }
        });
    }
}
