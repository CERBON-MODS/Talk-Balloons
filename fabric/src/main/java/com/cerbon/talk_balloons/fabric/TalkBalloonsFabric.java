package com.cerbon.talk_balloons.fabric;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.fabric.event.TBClientEvents;
import com.cerbon.talk_balloons.fabric.event.TBServerEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class TalkBalloonsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        TalkBalloons.init();
        TBServerEvents.init();
    }

    @Override
    public void onInitializeClient() {
        TBClientEvents.init();
        TBConfig.ConfigGuiHandler.init();
    }
}