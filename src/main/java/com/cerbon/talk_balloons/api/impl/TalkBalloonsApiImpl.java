package com.cerbon.talk_balloons.api.impl;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.Internal
public class TalkBalloonsApiImpl implements TalkBalloonsApi {
    @Override
    public void createBalloonMessage(Player player, String text, int duration) {
        ((ITalkBalloonsPlayer) player).talk_balloons$createBalloonMessage(text, duration);
    }

    @Override
    public void createBalloonMessage(Player player, Component text, int duration) {
        ((ITalkBalloonsPlayer) player).talk_balloons$createBalloonMessage(text, duration);
    }

    @Override
    public Collection<Component> getBalloonMessages(Player player) {
        return ((ITalkBalloonsPlayer) player).talk_balloons$getBalloonMessages();
    }
}
