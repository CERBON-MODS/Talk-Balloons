package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
//? if < 1.21.6 {
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//?} else {
/*import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
*///?}
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TBServerEventsForge {
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

    @SubscribeEvent(priority = /*? if < 1.21.6 {*/EventPriority/*?} else {*//*Priority*//*?}*/.HIGHEST)
    public static void onPlayerChat(ServerChatEvent event) {
        TalkBalloonsApi.INSTANCE.broadcastBalloonMessage(event.getPlayer(),
            //? if >= 1.19.2 {
            event.getRawText()
            //?} else {
            /*event.getMessage()
            *///?}
        );
    }
}
