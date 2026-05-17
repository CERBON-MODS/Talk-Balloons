package com.cerbon.talk_balloons.fabric;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.cerbon.talk_balloons.TalkBalloons;
//? if < 1.21.9 {
import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager;
import net.minecraft.server.packs.resources.ResourceManager;
//? }
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager;
import com.cerbon.talk_balloons.compat.CompatHandler;
import com.cerbon.talk_balloons.fabric.event.TBClientEvents;
import com.cerbon.talk_balloons.fabric.event.TBServerEvents;
import com.cerbon.talk_balloons.fabric.network.FabricNetworkRegistry;

//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation;
 //?} else {
/*import net.minecraft.resources.Identifier;
*///?}
import net.minecraft.server.packs.PackType;
//? if < 1.21.4 {
import net.minecraft.util.profiling.ProfilerFiller;
 //? }

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;

public class TalkBalloonsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        FabricNetworkRegistry.init();
        TalkBalloons.init();
        TBServerEvents.init();
        CompatHandler.isIrisLoaded = FabricLoader.getInstance().isModLoaded("iris");
    }

    @Override
    public void onInitializeClient() {
        TBClientEvents.init();
        //? if < 1.21.9 {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ getFabricId() {
                    return BalloonSpriteManager.ID;
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, /*? if < 1.21.4 {*/ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,/*? }*/ Executor backgroundExecutor, Executor gameExecutor) {
                    return BalloonRenderer.SPRITE_MANAGER.reload(preparationBarrier, resourceManager, /*? if < 1.21.4 {*/preparationsProfiler, reloadProfiler,/*? }*/ backgroundExecutor, gameExecutor);
                }
            });
        //? }

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ getFabricId() {
                    return BalloonStyleManager.ID;
                }

                //? if < 1.21.9 {
                @Override
                public CompletableFuture<Void> reload(
                    PreparationBarrier preparationBarrier, ResourceManager resourceManager, /*? if < 1.21.4 {*/ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,/*? }*/ Executor backgroundExecutor, Executor gameExecutor) {
                    return BalloonStyleManager.INSTANCE.reload(preparationBarrier, resourceManager, /*? if < 1.21.4 {*/preparationsProfiler, reloadProfiler,/*? }*/ backgroundExecutor, gameExecutor);
                }
                //? } else {

                /*@Override
                public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier barrier, Executor applyExecutor) {
                    return BalloonStyleManager.INSTANCE.reload(sharedState, executor, barrier, applyExecutor);
                }

                *///? }
            });
    }
}
