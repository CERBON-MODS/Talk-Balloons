package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.util.TBConstants;
import com.cerbon.talk_balloons.util.vad.SileroVadHandler;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TBClientEventsForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SileroVadHandler.loadVadDetector();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(TBConfig.class, parent).get()));
    }
}
