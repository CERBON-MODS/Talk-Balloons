package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.client.config.TBConfigGuiKt;
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager;
//? if <= 1.18.2 {
/*import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ScreenOpenEvent;
*///?} else {
import net.minecraftforge.client.ConfigScreenHandler;
//?}
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
//? if < 1.21.6 {
import net.minecraftforge.eventbus.api.SubscribeEvent;
//?} else {
/*import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
*///?}
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TBClientEventsForge {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //? if <= 1.18.2 {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> {
        *///?} else {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
        //?}
            return TBConfigGuiKt.generateConfigGui(parent);
        }));
    }

    @SubscribeEvent
    public static void onRegisterResourceReloaders(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BalloonRenderer.SPRITE_MANAGER);
        event.registerReloadListener(BalloonStyleManager.INSTANCE);
    }

    public static class TBForgeClientEvents {
        @SubscribeEvent
        public static void onPlayerDisconnect(/*? if <= 1.18.2 {*//*ClientPlayerNetworkEvent.LoggedOutEvent*//*?} else {*/ClientPlayerNetworkEvent.LoggingOut/*?}*/ event) {
            TalkBalloonsClient.onClientDisconnect();
        }

        @SubscribeEvent
        public static void onRenderEntities(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
            BalloonRenderer.renderBalloons();
        }
    }
}
