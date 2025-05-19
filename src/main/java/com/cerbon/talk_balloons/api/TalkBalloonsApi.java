package com.cerbon.talk_balloons.api;

import com.cerbon.talk_balloons.api.impl.TalkBalloonsApiImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public interface TalkBalloonsApi {
    TalkBalloonsApi INSTANCE = new TalkBalloonsApiImpl();

    void createBalloonMessage(Player player, String text, int duration);
    void createBalloonMessage(Player player, Component text, int duration);
    Collection<Component> getBalloonMessages(Player player);
}
