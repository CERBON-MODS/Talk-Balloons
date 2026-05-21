package com.cerbon.talk_balloons.fabric.event;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.network.TBServerPacketHandler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class TBServerEvents {
    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            TalkBalloons.onPlayerJoin(listener.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
            TalkBalloons.onPlayerDisconnect(listener.getPlayer().getUUID());
        });

        ServerPlayNetworking.registerGlobalReceiver(TBPackets.STATUS.type(), (packet, ctx) -> {
            TBServerPacketHandler.handleStatus(ctx.player(), packet);
        });

        ServerPlayNetworking.registerGlobalReceiver(TBPackets.SYNC_BALLOON_CONFIG.type(), (packet, ctx) -> {
            TBServerPacketHandler.handleSyncBalloonConfig(ctx.player(), packet);
        });
    }
}
