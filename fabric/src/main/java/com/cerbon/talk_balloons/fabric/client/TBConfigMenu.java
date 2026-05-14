package com.cerbon.talk_balloons.fabric.client;

import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class TBConfigMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return null;
        };
    }
}
