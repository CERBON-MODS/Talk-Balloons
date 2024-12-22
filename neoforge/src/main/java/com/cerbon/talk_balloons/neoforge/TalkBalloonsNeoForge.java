package com.cerbon.talk_balloons.neoforge;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.TBConstants;
import net.neoforged.fml.common.Mod;

@Mod(TBConstants.MOD_ID)
public class TalkBalloonsNeoForge {

    public TalkBalloonsNeoForge() {
        TalkBalloons.init();
    }
}