package com.cerbon.talk_balloons.fabric.event;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.network.TBPackets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

//? if >= 26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
*///? } else if >= 1.21.10 && <= 1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///? } else {
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
//? }

public class TBClientEvents {
    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((listener, mc) -> {
            TalkBalloonsClient.onClientDisconnect();
        });

        //? if <= 1.21.11 {
        WorldRenderEvents.AFTER_ENTITIES
        //? } else {
        /*LevelRenderEvents.AFTER_SOLID_FEATURES
        *///? }
            .register(context -> {
                BalloonRenderer.renderBalloons();
            });
    }
}
