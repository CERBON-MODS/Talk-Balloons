package com.cerbon.talk_balloons.forge.event;

import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
//? if <= 1.18.2 {
/*import net.minecraftforge.client.ConfigGuiHandler;
*///?} else {
import net.minecraftforge.client.ConfigScreenHandler;
//?}
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TBClientEventsForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //? if <= 1.18.2 {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(TBConfig.class, parent).get()));
        *///?} else {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(TBConfig.class, parent).get()));
        //?}
    }
}
