package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
//? if < 1.20.6 {
import net.minecraft.client.multiplayer.ClientPacketListener;
//? } else {
/*import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
*///? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if < 1.19 {
/*import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;

*///?}

@Mixin(
    //? if < 1.20.6 {
    ClientPacketListener.class
    //? } else {
    /*ClientCommonPacketListenerImpl.class
    *///? }
)
public class ClientPacketListenerMixin {
    @Inject(method = "handleDisconnect", at = @At("TAIL"))
    private void talk_balloons$handleDisconnect(CallbackInfo ci) {

    }

    //? if < 1.19 {
    /*@Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleChat", at = @At("TAIL"))
    private void getChatMessage(ClientboundChatPacket packet, CallbackInfo ci) {
        if (packet.getSender() == null || packet.getType() != ChatType.CHAT)
            return;

        if (!(packet.getMessage() instanceof TranslatableComponent translatable))
            return;

        if (!translatable.getKey().equals("chat.type.text"))
            return;

        if (translatable.getArgs().length < 2)
            return;

        // Extract the message from the translation key
        var arg2 = translatable.getArgs()[1];
        var message = arg2 instanceof Component component1 ? component1.getString() : arg2.toString();

        Level level = this.minecraft.level;
        if (level == null) return;

        Player thisClientPlayer = this.minecraft.player;
        if (thisClientPlayer != null && thisClientPlayer.getUUID() == packet.getSender() && !TalkBalloons.config.showOwnBalloon) return;

        Player player = level.getPlayerByUUID(packet.getSender());
        if (player == null) return;

        ((ITalkBalloonsPlayer) player).talk_balloons$createBalloonMessage(message, TalkBalloons.config.balloonAge * 20);
    }
    *///?}
}