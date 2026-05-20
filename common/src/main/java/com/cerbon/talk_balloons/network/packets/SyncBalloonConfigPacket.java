package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncBalloonConfigPacket(
    SynchronizedConfigData data
) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, SyncBalloonConfigPacket> CODEC = StreamCodec.composite(
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigPacket::data,
        SyncBalloonConfigPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TBPackets.SYNC_BALLOON_CONFIG.type();
    }
}
