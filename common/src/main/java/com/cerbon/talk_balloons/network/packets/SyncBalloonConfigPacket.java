package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.v2.codec.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.api.v2.packet.PacketDefinition;

import net.minecraft.network.FriendlyByteBuf;

public record SyncBalloonConfigPacket(
    SynchronizedConfigData data
) implements NetworkPacket {
    public static final NetworkCodec<FriendlyByteBuf, SyncBalloonConfigPacket> CODEC = CompositeCodecs.composite(
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigPacket::data,
        SyncBalloonConfigPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends ByteBuf, ? extends NetworkPacket> getDefinition() {
        return TBPackets.SYNC_BALLOON_CONFIG;
    }
}
