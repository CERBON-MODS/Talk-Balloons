package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.v2.codec.CompositeCodecs;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodecs;
import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.api.v2.packet.PacketDefinition;

import net.minecraft.network.FriendlyByteBuf;

public record TalkBalloonsStatusClientPacket(
    int protocolVersion
) implements NetworkPacket {
    public static final NetworkCodec<FriendlyByteBuf, TalkBalloonsStatusClientPacket> CODEC = CompositeCodecs.composite(
        NetworkCodecs.VAR_INT, TalkBalloonsStatusClientPacket::protocolVersion,
        TalkBalloonsStatusClientPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends ByteBuf, ? extends NetworkPacket> getDefinition() {
        return TBPackets.STATUS_CLIENT;
    }
}
