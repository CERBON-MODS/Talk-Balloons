package com.cerbon.talk_balloons.mixin;

//? if >= 1.19.2 {
import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
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

        if (!(component.getContents() instanceof TranslatableContents contents))
            return true;

        if (!contents.getKey().equals("chat.type.text"))
            return true;

        if (contents.getArgs().length < 2)
            return true;

        var arg = contents.getArgs()[0];
        var arg2 = contents.getArgs()[1];

        var message = arg2 instanceof Component component1 ? component1.getString() : arg2.toString();

        if (!(arg instanceof MutableComponent mutableComponent))
            return true;

        if (mutableComponent.getStyle().getHoverEvent() == null)
            return true;

        if (mutableComponent.getStyle().getHoverEvent()/*? if <= 1.21.4 {*/.getAction()/*?} else {*//*.action()*//*?}*/ != HoverEvent.Action.SHOW_ENTITY)
            return true;

        //? if <= 1.21.4 {
        var value = mutableComponent.getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_ENTITY);
        //?} else {
        /*var value = ((HoverEvent.ShowEntity) mutableComponent.getStyle().getHoverEvent()).entity();
        *///?}

        if (value == null || value.type != EntityType.PLAYER)
            return true;

        UUID uuid = value/*? if <= 1.21.4 {*/.id/*?} else {*//*.uuid*//*?}*/;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        var config = TalkBalloonsClient.syncedConfigs.getPlayerConfig(uuid);

        if (level == null)
            return !config.onlyDisplayBalloons();

        Player thisClientPlayer = minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == uuid && !TalkBalloons.config.showOwnBalloon)
            return !config.onlyDisplayBalloons();

        Player player = level.getPlayerByUUID(uuid);
        if (player == null)
            return !config.onlyDisplayBalloons();

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, Component.literal(message), TalkBalloons.config.balloonAge * 20);
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
