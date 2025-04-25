package com.cerbon.talk_balloons.fabric.event;

import com.cerbon.talk_balloons.TalkBalloons;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class TBServerEvents {
    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            TalkBalloons.onPlayerJoin(listener.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
            TalkBalloons.onPlayerDisconnect(listener.getPlayer().getUUID());
        });
    }
}
