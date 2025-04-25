package com.cerbon.talk_balloons.neoforge.event;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
//? if > 1.20.4 {
/*import net.neoforged.fml.common.EventBusSubscriber;
*///?} else {
import net.neoforged.fml.common.Mod.EventBusSubscriber;
//?}

@EventBusSubscriber(modid = TBConstants.MOD_ID,
    //? if <= 1.20.4 {
    bus = EventBusSubscriber.Bus.FORGE
    //?} else {
    /*bus = EventBusSubscriber.Bus.GAME
    *///?}
)
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
}
