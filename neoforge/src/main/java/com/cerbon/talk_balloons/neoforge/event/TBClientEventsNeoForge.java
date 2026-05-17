package com.cerbon.talk_balloons.neoforge.event;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.client.config.TBConfigGuiKt;
import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager;
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
//? if < 1.21.4 {
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
//? } else {
/*import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
*///? }
import net.neoforged.neoforge.client.event.ScreenEvent;
//? if > 1.20.4 {
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else {
/*import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.client.ConfigScreenHandler;
*///?}
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TBConstants.MOD_ID,
    //? if < 1.21.6 {
    bus = EventBusSubscriber.Bus.MOD,
    //?}
    value = Dist.CLIENT)
public class TBClientEventsNeoForge {
    private static Screen configScreenToHandle;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //? if > 1.20.4 {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> {
        //?} else {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
        *///?}
            return TBConfigGuiKt.generateConfigGui(parent);
        //? if > 1.20.4 {
        });
        //?} else {
        /*}));
        *///?}
    }

    //? if < 1.21.4 {
    @SubscribeEvent
    public static void onRegisterResourceReloaders(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BalloonRenderer.SPRITE_MANAGER);
        event.registerReloadListener(BalloonStyleManager.INSTANCE);
    }
    //? } else {
    /*@SubscribeEvent
    public static void onRegisterResourceReloaders(AddClientReloadListenersEvent event) {
        event.addListener(BalloonSpriteManager.ID, BalloonRenderer.SPRITE_MANAGER);
        event.addListener(BalloonStyleManager.ID, BalloonStyleManager.INSTANCE);
    }
    *///? }

    @EventBusSubscriber(modid = TBConstants.MOD_ID,
        //? if <= 1.20.4 {
        /*bus = EventBusSubscriber.Bus.FORGE,
        *///?} else if < 1.21.6 {
        bus = EventBusSubscriber.Bus.GAME,
        //?}
        value = Dist.CLIENT)
    public static class TBNeoForgeClientEvents {
        @SubscribeEvent
        public static void onPlayerDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
            TalkBalloonsClient.onClientDisconnect();
        }
    }
}
