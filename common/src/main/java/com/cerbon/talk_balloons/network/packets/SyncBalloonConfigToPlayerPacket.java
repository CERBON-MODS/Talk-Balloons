package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record SyncBalloonConfigToPlayerPacket(
    UUID uuid,
    SynchronizedConfigData data
) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, SyncBalloonConfigToPlayerPacket> CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC, SyncBalloonConfigToPlayerPacket::uuid,
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigToPlayerPacket::data,
        SyncBalloonConfigToPlayerPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TBPackets.SYNC_CONFIG_TO_PLAYER.type();
    }
}
