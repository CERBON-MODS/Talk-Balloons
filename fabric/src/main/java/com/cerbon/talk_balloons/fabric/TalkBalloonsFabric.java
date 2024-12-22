package com.cerbon.talk_balloons.fabric;

import com.cerbon.talk_balloons.TalkBalloons;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class TalkBalloonsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        TalkBalloons.init();
    }

    @Override
    public void onInitializeClient() {}
}