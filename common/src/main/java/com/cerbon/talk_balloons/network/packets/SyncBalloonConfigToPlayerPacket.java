package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.v2.codec.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodecs;
import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.api.v2.packet.PacketDefinition;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public record SyncBalloonConfigToPlayerPacket(
    UUID uuid,
    SynchronizedConfigData data
) implements NetworkPacket {
    public static final NetworkCodec<FriendlyByteBuf, SyncBalloonConfigToPlayerPacket> CODEC = CompositeCodecs.composite(
        NetworkCodecs.UUID, SyncBalloonConfigToPlayerPacket::uuid,
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigToPlayerPacket::data,
        SyncBalloonConfigToPlayerPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends ByteBuf, ? extends NetworkPacket> getDefinition() {
        return TBPackets.SYNC_CONFIG_TO_PLAYER;
    }
}
