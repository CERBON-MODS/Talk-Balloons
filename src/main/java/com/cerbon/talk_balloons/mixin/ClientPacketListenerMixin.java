package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
//? if < 1.20.6 {
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientPacketListener;
//?} else {
/*import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if < 1.19 {
/*import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

*///?}

@Mixin(
    //? if < 1.20.6 {
    ClientPacketListener.class
    //?} else {
    /*ClientCommonPacketListenerImpl.class
    *///?}
)
public class ClientPacketListenerMixin {
    //? if < 1.19 {
    /*@Shadow @Final private Minecraft minecraft;

    @WrapWithCondition(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;handleChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V"))
    private boolean getChatMessage(Gui instance, ChatType chatType, Component component, UUID sender) {
        if (sender == null || chatType != ChatType.CHAT)
            return true;

        if (!(component instanceof TranslatableComponent translatable))
            return true;

        if (!translatable.getKey().equals("chat.type.text"))
            return true;

        if (translatable.getArgs().length < 2)
            return true;

        var config = TalkBalloonsClient.syncedConfigs.getPlayerConfig(sender);
        if (TalkBalloonsClient.hasServerSupport())
            return !config.onlyDisplayBalloons();

        // Extract the message from the translation key
        var arg2 = translatable.getArgs()[1];
        var message = arg2 instanceof Component component1 ? component1.getString() : arg2.toString();

        Level level = this.minecraft.level;
        if (level == null)
            return !config.onlyDisplayBalloons();

        Player thisClientPlayer = this.minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == sender && !TalkBalloons.config.showOwnBalloon)
            return !config.onlyDisplayBalloons();

        Player player = level.getPlayerByUUID(sender);
        if (player == null)
            return !config.onlyDisplayBalloons();

        ((ITalkBalloonsPlayer) player).talk_balloons$createBalloonMessage(message, TalkBalloons.config.balloonAge * 20);
        return !config.onlyDisplayBalloons();
    }
    *///?}
}