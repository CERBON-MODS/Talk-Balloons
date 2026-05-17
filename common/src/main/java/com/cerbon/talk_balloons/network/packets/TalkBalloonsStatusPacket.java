package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TalkBalloonsStatusPacket(
    int protocolVersion
) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, TalkBalloonsStatusPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, TalkBalloonsStatusPacket::protocolVersion,
        TalkBalloonsStatusPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TBPackets.STATUS.type();
    }
}
