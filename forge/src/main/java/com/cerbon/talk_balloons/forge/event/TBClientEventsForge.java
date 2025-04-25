package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
//? if <= 1.18.2 {
/*import net.minecraftforge.client.ConfigGuiHandler;
*///?} else {
import net.minecraftforge.client.ConfigScreenHandler;
//?}
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TBClientEventsForge {
    private static Screen configScreenToHandle;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //? if <= 1.18.2 {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> {
        *///?} else {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
        //?}
            var screen = AutoConfig.getConfigScreen(TBConfig.class, parent).get();
            configScreenToHandle = screen;
            return screen;
        }));

        TBConfig.ConfigGuiHandler.init();
    }

    @Mod.EventBusSubscriber(modid = TBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class TBForgeClientEvents {
        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            if (configScreenToHandle != null && TalkBalloonsClient.hasServerSupport() && event.getScreen() == configScreenToHandle) {
                TBClientPacketHandler.syncBalloonConfig();
            }
        }

        @SubscribeEvent
        public static void onPlayerDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
            TalkBalloonsClient.onClientDisconnect();
        }
    }
}
