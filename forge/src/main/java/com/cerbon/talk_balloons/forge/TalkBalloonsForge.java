package com.cerbon.talk_balloons.forge;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.forge.event.TBClientEventsForge;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(TBConstants.MOD_ID)
public class TalkBalloonsForge {

    public TalkBalloonsForge() {
        TalkBalloons.init();
    }
}