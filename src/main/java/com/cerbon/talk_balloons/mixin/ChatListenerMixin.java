package com.cerbon.talk_balloons.mixin;

//? if >= 1.19.2 {
import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
//? if >= 1.20
import com.mojang.authlib.GameProfile;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;
import java.util.UUID;
//?}

//? if >= 1.19.2
@Mixin(ChatListener.class)
public class ChatListenerMixin {
    //? if >= 1.19.2 {
    // Try to extract the player from the sent message, to workaround an incompatibility with No Chat Reports.
    @Inject(method = "handleSystemMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void tryGetChatMessageNCR(Component component, boolean isOverlay, CallbackInfo ci) {
        if (TalkBalloonsClient.hasServerSupport())
            return;

        if (!(component.getContents() instanceof TranslatableContents contents))
            return;

        if (!contents.getKey().equals("chat.type.text"))
            return;

        if (contents.getArgs().length < 2)
            return;

        var arg = contents.getArgs()[0];
        var arg2 = contents.getArgs()[1];

        var message = arg2 instanceof Component component1 ? component1.getString() : arg2.toString();

        if (!(arg instanceof MutableComponent mutableComponent))
            return;

        if (mutableComponent.getStyle().getHoverEvent() == null)
            return;

        if (mutableComponent.getStyle().getHoverEvent().getAction() != HoverEvent.Action.SHOW_ENTITY)
            return;

        var value = mutableComponent.getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_ENTITY);

        if (value == null || value.type != EntityType.PLAYER)
            return;

        UUID uuid = value.id;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null)
            return;

        Player thisClientPlayer = minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == uuid && !TalkBalloons.config.showOwnBalloon)
            return;

        Player player = level.getPlayerByUUID(uuid);
        if (player == null)
            return;

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, Component.literal(message), TalkBalloons.config.balloonAge * 20);
    }

    @Inject(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getChat()Lnet/minecraft/client/gui/components/ChatComponent;", ordinal = 0))
    private void getChatMessage(ChatType.Bound boundChatType, PlayerChatMessage chatMessage, Component decoratedServerContent,
                                //? if >= 1.20 {
                                GameProfile gameProfile,
                                //?} else {
                                /*PlayerInfo gameProfile,
                                *///?}
                                boolean onlyShowSecureChat, Instant timestamp, CallbackInfoReturnable<Boolean> cir) {
        if (TalkBalloonsClient.hasServerSupport())
            return;

        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) return;

        Player thisClientPlayer = minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == chatMessage/*? if <= 1.19.2 {*//*.signedHeader()*//*?}*/.sender() && !TalkBalloons.config.showOwnBalloon) return;

        Player player = level.getPlayerByUUID(chatMessage/*? if <= 1.19.2 {*//*.signedHeader()*//*?}*/.sender());
        if (player == null) return;

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, chatMessage/*? if <= 1.19.2 {*//*.signedContent().plain()*//*?} else {*/.decoratedContent()/*?}*/, TalkBalloons.config.balloonAge * 20);
    }
    //?}
}
