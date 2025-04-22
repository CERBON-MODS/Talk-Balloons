package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.network.CustomCodecs;
import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;

public record SyncBalloonConfigPacket(
    int textColor,
    int balloonColor,
    ResourceLocation balloonTexture,
    ResourceLocation font
) implements NetworkPacket {
    public static final NetworkCodec<SyncBalloonConfigPacket, FriendlyByteBuf> CODEC = CompositeCodecs.composite(
        NetworkCodecs.VAR_INT, SyncBalloonConfigPacket::textColor,
        NetworkCodecs.VAR_INT, SyncBalloonConfigPacket::balloonColor,
        CustomCodecs.RESOURCE_LOCATION, SyncBalloonConfigPacket::balloonTexture,
        CustomCodecs.RESOURCE_LOCATION, SyncBalloonConfigPacket::font,
        SyncBalloonConfigPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.SYNC_BALLOON_CONFIG;
    }
}
