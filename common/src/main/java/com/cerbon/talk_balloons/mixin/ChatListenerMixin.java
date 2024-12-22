package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(ChatListener.class)
public class ChatListenerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getChat()Lnet/minecraft/client/gui/components/ChatComponent;", ordinal = 0))
    private void getChatMessage(ChatType.Bound boundChatType, PlayerChatMessage chatMessage, Component decoratedServerContent, GameProfile gameProfile, boolean onlyShowSecureChat, Instant timestamp, CallbackInfoReturnable<Boolean> cir) {
        Level level = this.minecraft.level;
        if (level == null) return;

        Player thisClientPlayer = this.minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == chatMessage.sender() && !TalkBalloons.config.showOwnBalloon) return;

        Player player = level.getPlayerByUUID(chatMessage.sender());
        if (player == null) return;

        ((IAbstractClientPlayer) player).createBalloonMessage(chatMessage.signedContent(), TalkBalloons.config.balloonAge * 20);
    }
}
