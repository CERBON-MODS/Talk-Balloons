package com.cerbon.talk_balloons.mixin;

//? if >= 1.19.2 {
import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.util.ChatUtils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;
//?}

//? if >= 1.19.2
@Mixin(ChatListener.class)
public class ChatListenerMixin {
    //? if >= 1.19.2 {
    // Try to extract the player from the sent message, to workaround an incompatibility with No Chat Reports.
    @WrapWithCondition(method = "handleSystemMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;)V"))
    private boolean tryGetChatMessageNCR(ChatComponent instance, Component component) {
        if (TalkBalloonsClient.hasServerSupport())
            return true;

        ChatUtils.MessageContents contents = ChatUtils.tryExtractContents(component);

        if (contents == null)
            return true;

        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        var config = TalkBalloonsClient.syncedConfigs.getPlayerConfig(contents.sender());

        if (level == null)
            return !config.onlyDisplayBalloons();

        Player thisClientPlayer = minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == contents.sender() && !TalkBalloons.config.showOwnBalloon)
            return !config.onlyDisplayBalloons();

        Player player = level.getPlayerByUUID(contents.sender());
        if (player == null)
            return !config.onlyDisplayBalloons();

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, Component.literal(contents.contents()), TalkBalloons.config.balloonAge * 20);
        return !config.onlyDisplayBalloons();
    }

    @WrapWithCondition(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", ordinal = 0))
    private boolean getChatMessage(ChatComponent instance, Component component, MessageSignature messageSignature, GuiMessageTag guiMessageTag, @Local(argsOnly = true) PlayerChatMessage chatMessage) {
        var config = TalkBalloonsClient.syncedConfigs.getPlayerConfig(chatMessage/*? if <= 1.19.2 {*//*.signedHeader()*//*?}*/.sender());

        if (TalkBalloonsClient.hasServerSupport())
            return !config.onlyDisplayBalloons();

        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null)
            return !config.onlyDisplayBalloons();

        Player thisClientPlayer = minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == chatMessage/*? if <= 1.19.2 {*//*.signedHeader()*//*?}*/.sender() && !TalkBalloons.config.showOwnBalloon)
            return !config.onlyDisplayBalloons();

        Player player = level.getPlayerByUUID(chatMessage/*? if <= 1.19.2 {*//*.signedHeader()*//*?}*/.sender());
        if (player == null)
            return !config.onlyDisplayBalloons();

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, chatMessage/*? if <= 1.19.2 {*//*.signedContent().plain()*//*?} else {*/.decoratedContent()/*?}*/, TalkBalloons.config.balloonAge * 20);

        return !config.onlyDisplayBalloons();
    }
    //?}
}
