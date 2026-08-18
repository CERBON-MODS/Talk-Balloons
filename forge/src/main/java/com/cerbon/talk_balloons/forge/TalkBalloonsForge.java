package com.cerbon.talk_balloons.forge;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.compat.CompatHandler;
import com.cerbon.talk_balloons.forge.event.TBClientEventsForge;
import com.cerbon.talk_balloons.forge.event.TBServerEventsForge;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;

@Mod(TBConstants.MOD_ID)
public class TalkBalloonsForge {
    public TalkBalloonsForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        TalkBalloons.init();
        CompatHandler.isIrisLoaded = isModLoaded("iris") || isModLoaded("oculus");

        if (
            //? if < 1.21.10 {
            FMLLoader.getDist()
                //? } else {
                /*FMLLoader.getCurrent().getDist()
                 *///? }
                == Dist.CLIENT
        ) {
            MinecraftForge.EVENT_BUS.register(TBClientEventsForge.TBForgeClientEvents.class);
            bus.register(TBClientEventsForge.class);
        }

        MinecraftForge.EVENT_BUS.register(TBServerEventsForge.class);
    }

    private boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId) || LoadingModList.get().getModFileById(modId) != null;
    }
}
