package com.cerbon.talk_balloons.api.impl;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
import net.minecraft.network.chat.Component;
//? if < 1.19 {
/*import net.minecraft.network.chat.TextComponent;
 *///?}
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

import java.util.Collection;

@ApiStatus.Internal
public class TalkBalloonsApiImpl implements TalkBalloonsApi {
    @Override
    public int getDefaultDuration() {
        return TalkBalloons.config.balloonAge;
    }

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

    @Override
    public void broadcastBalloonMessage(ServerPlayer source, String text, int duration) {
        this.broadcastBalloonMessage(
            source,
            //? if >= 1.19 {
            Component.literal(text),
            //?} else {
            /*new TextComponent(text),
             *///?}
            duration
        );
    }

    @Override
    public void broadcastBalloonMessage(ServerPlayer source, Component text, int duration) {
        var balloonPacket = new CreateBalloonPacket(source.getUUID(), text, duration);

        MinecraftServer server = //? if >= 1.21.9 {
            /*source.level().getServer();
            *///?} else {
            source.getServer();
            //?}

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (TalkBalloons.playerHasSupport(player.getUUID())) {
                VanillaPacketSender.sendToPlayer(player, balloonPacket);
            }
        }
    }
}
