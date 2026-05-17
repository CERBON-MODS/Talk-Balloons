package com.cerbon.talk_balloons.fabric.event;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.network.TBPackets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

public class TBClientEvents {
    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((listener, mc) -> {
            TalkBalloonsClient.onClientDisconnect();
        });

        ClientPlayNetworking.registerGlobalReceiver(TBPackets.STATUS.type(), (packet, ctx) -> {
            TBClientPacketHandler.handleStatus(packet);
        });

        ClientPlayNetworking.registerGlobalReceiver(TBPackets.CREATE_BALLOON.type(), (packet, ctx) -> {
            TBClientPacketHandler.handleCreateBalloon(packet);
        });

        ClientPlayNetworking.registerGlobalReceiver(TBPackets.SYNC_CONFIG_TO_PLAYER.type(), (packet, ctx) -> {
            TBClientPacketHandler.handleSyncConfigToPlayer(packet);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            BalloonRenderer.renderBalloons();
        });
    }
}
