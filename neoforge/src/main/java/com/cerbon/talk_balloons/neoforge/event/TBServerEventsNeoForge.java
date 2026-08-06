package com.cerbon.talk_balloons.neoforge.event;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class TBServerEventsNeoForge {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            TalkBalloons.onPlayerJoin(player);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            TalkBalloons.onPlayerDisconnect(player.getUUID());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerChat(ServerChatEvent event) {
        TalkBalloonsApi.INSTANCE.broadcastBalloonMessage(event.getPlayer(), event.getRawText());
    }
}
