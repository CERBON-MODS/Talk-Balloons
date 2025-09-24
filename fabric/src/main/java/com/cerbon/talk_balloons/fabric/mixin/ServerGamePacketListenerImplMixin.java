package com.cerbon.talk_balloons.fabric.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
//? if < 1.19
/*import net.minecraft.network.chat.TextComponent;*/
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//? if >= 1.19.2
import net.minecraft.network.chat.PlayerChatMessage;
//? if >= 1.20.2 {
/*import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
*///?}
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

import java.util.UUID;
import java.util.function.Function;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin/*? if >= 1.20.2 {*/ /*extends ServerCommonPacketListenerImpl*//*?}*/ {
    //? if <= 1.20.1
    @Shadow @Final private MinecraftServer server;

    @Shadow public ServerPlayer player;

    //? if >= 1.20.2 {
    /*public ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }
    *///?}

    @Shadow public abstract ServerPlayer getPlayer();

    //? if >= 1.19.2 {
    @WrapWithCondition(method = {"method_45064"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;)V"))
    private boolean talk_balloons$sendBalloonToPlayers(ServerGamePacketListenerImpl instance, PlayerChatMessage message) {
        var text = /*? if <= 1.19.2 {*//*Component.literal(message.signedContent().plain())*//*?} else {*/message.decoratedContent()/*?}*/;
        TalkBalloonsApi.INSTANCE.broadcastBalloonMessage(this.getPlayer(), text);

        return true;
    }
    //?} else {
    /*@WrapWithCondition(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private boolean talk_balloons$sendBalloonToPlayers(PlayerList instance, Component fakeText, Function<ServerPlayer, Component> serverPlayerComponentFunction, ChatType message, UUID filter, @Local(argsOnly = true) TextFilter.FilteredText text) {
        TalkBalloonsApi.INSTANCE.broadcastBalloonMessage(this.getPlayer(), text.getRaw());

        return true;
    }
    *///?}
}
