package com.cerbon.talk_balloons.fabric.event;

import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class TBClientEvents {
    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((listener, mc) -> {
            TalkBalloonsClient.onClientDisconnect();
        });
    }
}
