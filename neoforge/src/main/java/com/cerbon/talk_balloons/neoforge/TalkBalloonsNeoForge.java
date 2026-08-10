package com.cerbon.talk_balloons.neoforge;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.compat.CompatHandler;
import com.cerbon.talk_balloons.neoforge.event.TBClientEventsNeoForge;
import com.cerbon.talk_balloons.neoforge.event.TBServerEventsNeoForge;
import com.cerbon.talk_balloons.util.TBConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TBConstants.MOD_ID)
public class TalkBalloonsNeoForge {
    public TalkBalloonsNeoForge(IEventBus bus) {
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
            NeoForge.EVENT_BUS.register(TBClientEventsNeoForge.TBNeoForgeClientEvents.class);
            bus.register(TBClientEventsNeoForge.class);
        }

        NeoForge.EVENT_BUS.register(TBServerEventsNeoForge.class);
    }

    private boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId) || LoadingModList.get().getModFileById(modId) != null;
    }
}
