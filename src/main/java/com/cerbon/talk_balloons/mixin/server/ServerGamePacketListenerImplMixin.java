package com.cerbon.talk_balloons.mixin.server;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
//? if >= 1.20.2
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin/*? if >= 1.20.2 { */ extends ServerCommonPacketListenerImpl/*? }*/ {
    //? if <= 1.20.1
    /*@Shadow @Final private MinecraftServer server;*/

    //? if >= 1.20.2 {
    public ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }
    //?}

    //? if <= 1.20.4 {
    /*@Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"))
    *///? } else {
    @Inject(method = "method_44900", at = @At("TAIL"))
    //? }
    private void talk_balloons$sendChatMessageToPlayers(
        ServerboundChatPacket packet,
        //? if >= 1.20.4
        Optional optional,
        CallbackInfo ci
    ) {
        var balloonPacket = new CreateBalloonPacket(Component.literal(packet.message()), -1);

        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
            if (TalkBalloons.playerHasSupport(player.getUUID())) {
                VanillaPacketSender.sendToPlayer(player, balloonPacket);
            }
        }
    }
}
