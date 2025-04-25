package com.cerbon.talk_balloons.fabric.client;

import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class TBConfigMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            var screen = AutoConfig.getConfigScreen(TBConfig.class, parent).get();

            ScreenEvents.AFTER_INIT.register((client, screen1, scaledWidth, scaledHeight) -> {
                if (screen1 == screen) {
                    ScreenEvents.remove(screen).register($ -> {
                        TBClientPacketHandler.syncBalloonConfig();
                    });
                }
            });

            return screen;
        };
    }
}
