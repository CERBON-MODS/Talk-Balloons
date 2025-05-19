package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.CustomCodecs;
import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;

public record SyncBalloonConfigPacket(
    SynchronizedConfigData data
) implements NetworkPacket {
    public static final NetworkCodec<SyncBalloonConfigPacket, FriendlyByteBuf> CODEC = CompositeCodecs.composite(
        SynchronizedConfigData.NETWORK_CODEC, SyncBalloonConfigPacket::data,
        SyncBalloonConfigPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.SYNC_BALLOON_CONFIG;
    }
}
