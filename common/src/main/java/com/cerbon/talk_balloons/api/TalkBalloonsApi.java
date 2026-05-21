package com.cerbon.talk_balloons.api;

import com.cerbon.talk_balloons.api.impl.TalkBalloonsApiImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public interface TalkBalloonsApi {
    TalkBalloonsApi INSTANCE = new TalkBalloonsApiImpl();

    int getDefaultDuration();

    default void createBalloonMessage(Player player, String text) {
        this.createBalloonMessage(player, text, this.getDefaultDuration());
    }

    default void createBalloonMessage(Player player, Component text) {
        this.createBalloonMessage(player, text, this.getDefaultDuration());
    }

    void createBalloonMessage(Player player, String text, int duration);
    void createBalloonMessage(Player player, Component text, int duration);
    Collection<Component> getBalloonMessages(Player player);

    default void broadcastBalloonMessage(ServerPlayer source, String text) {
        this.broadcastBalloonMessage(source, text, -1);
    }

    default void broadcastBalloonMessage(ServerPlayer source, Component text) {
        this.broadcastBalloonMessage(source, text, -1);
    }

    void broadcastBalloonMessage(ServerPlayer source, String text, int duration);
    void broadcastBalloonMessage(ServerPlayer source, Component text, int duration);
}
