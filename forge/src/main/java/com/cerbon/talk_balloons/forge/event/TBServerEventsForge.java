package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
}
