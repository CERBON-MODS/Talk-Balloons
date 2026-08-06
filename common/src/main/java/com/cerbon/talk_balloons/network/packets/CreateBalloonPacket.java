package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.v2.codec.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodecs;
import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.api.v2.packet.PacketDefinition;
import xyz.bluspring.modernnetworking.minecraft.api.v2.codec.MinecraftNetworkCodecs;

import net.minecraft.core.UUIDUtil;
//? if >= 1.20.6
//import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.UUID;

public record CreateBalloonPacket(
    UUID uuid,
    Component message,
    int balloonAge // If -1, use the client config's balloon age.
) implements NetworkPacket {
    public static final NetworkCodec</*? if >= 1.20.6 {*//*RegistryFriendlyByteBuf*//*?} else {*/FriendlyByteBuf/*?}*/, CreateBalloonPacket> CODEC = CompositeCodecs.composite(
        MinecraftNetworkCodecs.toNetworkCodec(UUIDUtil.STREAM_CODEC), CreateBalloonPacket::uuid,
        MinecraftNetworkCodecs.toNetworkCodec(ComponentSerialization.STREAM_CODEC), CreateBalloonPacket::message,
        NetworkCodecs.VAR_INT, CreateBalloonPacket::balloonAge,
        CreateBalloonPacket::new
    );

    public int getBalloonAge() {
        if (this.balloonAge() == -1) {
            return TalkBalloons.config.getBalloonAge() * 20;
        }

        return this.balloonAge();
    }

    @Override
    public @NotNull PacketDefinition<? extends ByteBuf, ? extends NetworkPacket> getDefinition() {
        return TBPackets.CREATE_BALLOON;
    }
}
