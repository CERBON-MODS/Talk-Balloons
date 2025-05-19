package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;

import java.util.UUID;

public record SyncBalloonConfigToPlayerPacket(
    UUID uuid,
    SynchronizedConfigData data
) implements NetworkPacket {
    public static final NetworkCodec<SyncBalloonConfigToPlayerPacket, FriendlyByteBuf> CODEC = CompositeCodecs.composite(
        NetworkCodecs.UUID, SyncBalloonConfigToPlayerPacket::uuid,
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigToPlayerPacket::data,
        SyncBalloonConfigToPlayerPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.SYNC_CONFIG_TO_PLAYER;
    }
}
