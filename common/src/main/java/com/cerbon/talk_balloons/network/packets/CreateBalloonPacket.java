package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
//? if >= 1.20.6
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CreateBalloonPacket(
    UUID uuid,
    Component message,
    int balloonAge // If -1, use the client config's balloon age.
) implements CustomPacketPayload {
    public static final StreamCodec</*? if >= 1.20.6 {*/RegistryFriendlyByteBuf/*?} else {*//*FriendlyByteBuf*//*?}*/, CreateBalloonPacket> CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC, CreateBalloonPacket::uuid,
        ComponentSerialization.STREAM_CODEC, CreateBalloonPacket::message,
        ByteBufCodecs.VAR_INT, CreateBalloonPacket::balloonAge,
        CreateBalloonPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TBPackets.CREATE_BALLOON.type();
    }

    public int getBalloonAge() {
        if (this.balloonAge() == -1) {
            return TalkBalloons.config.getBalloonAge() * 20;
        }

        return this.balloonAge();
    }
}
