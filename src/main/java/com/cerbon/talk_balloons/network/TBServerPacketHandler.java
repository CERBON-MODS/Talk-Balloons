package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.TalkBalloons;

public class TBServerPacketHandler {
    public static void init() {
        var registry = TBPackets.REGISTRY;

        registry.addServerboundHandler(TBPackets.STATUS, (packet, ctx) -> {
            if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
                TalkBalloons.addSupportedPlayer(ctx.getPlayer().getUUID());
            }
        });

        registry.addServerboundHandler(TBPackets.SYNC_BALLOON_CONFIG, (packet, ctx) -> {
            TalkBalloons.serverSyncedConfigs.setPlayerConfig(ctx.getPlayer().getUUID(), packet.data());
        });
    }
}
