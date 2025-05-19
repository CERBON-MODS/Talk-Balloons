package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;

public record TalkBalloonsStatusPacket(
    int protocolVersion
) implements NetworkPacket {
    public static final NetworkCodec<TalkBalloonsStatusPacket, FriendlyByteBuf> CODEC = CompositeCodecs.composite(
        NetworkCodecs.VAR_INT, TalkBalloonsStatusPacket::protocolVersion,
        TalkBalloonsStatusPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.STATUS;
    }
}
