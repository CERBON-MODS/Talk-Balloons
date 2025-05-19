package com.cerbon.talk_balloons.neoforge.event;

import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
//? if > 1.20.4 {
/*import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?} else {
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.client.ConfigScreenHandler;
//?}
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TBConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TBClientEventsNeoForge {
    private static Screen configScreenToHandle;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //? if > 1.20.4 {
        /*ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> {
        *///?} else {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
        //?}
            var screen = AutoConfig.getConfigScreen(TBConfig.class, parent).get();
            configScreenToHandle = screen;
            return screen;
        //? if > 1.20.4 {
        /*});
        *///?} else {
        }));
        //?}

        TBConfig.ConfigGuiHandler.init();
    }

    @EventBusSubscriber(modid = TBConstants.MOD_ID,
        //? if <= 1.20.4 {
        bus = EventBusSubscriber.Bus.FORGE,
        //?} else {
        /*bus = EventBusSubscriber.Bus.GAME,
        *///?}
        value = Dist.CLIENT)
    public static class TBNeoForgeClientEvents {
        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            if (configScreenToHandle != null && event.getScreen() == configScreenToHandle) {
                TBClientPacketHandler.syncBalloonConfig();
            }
        }

        @SubscribeEvent
        public static void onPlayerDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
            TalkBalloonsClient.onClientDisconnect();
        }
    }
}
