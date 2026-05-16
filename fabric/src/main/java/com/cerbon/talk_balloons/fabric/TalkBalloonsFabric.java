package com.cerbon.talk_balloons.fabric;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.resources.BalloonStyle;
import com.cerbon.talk_balloons.fabric.event.TBClientEvents;
import com.cerbon.talk_balloons.fabric.event.TBServerEvents;
import com.cerbon.talk_balloons.fabric.network.FabricNetworkRegistry;

import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasSourceTypeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

public class TalkBalloonsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        FabricNetworkRegistry.init();
        TalkBalloons.init();
        TBServerEvents.init();
    }

    @Override
    public void onInitializeClient() {
        TBClientEvents.init();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return TalkBalloons.id("balloon_sprite_manager");
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                    return BalloonRenderer.SPRITE_MANAGER.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
                }
            });
    }
}
