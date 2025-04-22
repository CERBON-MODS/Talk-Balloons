package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaCodecs;

public record CreateBalloonPacket(
    Component message,
    int balloonAge
) implements NetworkPacket {
    public static final NetworkCodec<CreateBalloonPacket, FriendlyByteBuf> CODEC = CompositeCodecs.composite(
        VanillaCodecs.COMPONENT, CreateBalloonPacket::message,
        NetworkCodecs.VAR_INT, CreateBalloonPacket::balloonAge,
        CreateBalloonPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.CREATE_BALLOON;
    }
}
