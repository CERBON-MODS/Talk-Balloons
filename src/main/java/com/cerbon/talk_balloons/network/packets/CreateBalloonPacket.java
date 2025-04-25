package com.cerbon.talk_balloons.network.packets;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.TBPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
//? if >= 1.20.6
/*import net.minecraft.network.RegistryFriendlyByteBuf;*/
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.bluspring.modernnetworking.api.*;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaCodecs;

import java.util.UUID;

public record CreateBalloonPacket(
    UUID uuid,
    Component message,
    int balloonAge // If -1, use the client config's balloon age.
) implements NetworkPacket {
    public static final NetworkCodec<CreateBalloonPacket, /*? if >= 1.20.6 {*//*RegistryFriendlyByteBuf*//*?} else {*/FriendlyByteBuf/*?}*/> CODEC = CompositeCodecs.composite(
        NetworkCodecs.UUID, CreateBalloonPacket::uuid,
        VanillaCodecs.COMPONENT, CreateBalloonPacket::message,
        NetworkCodecs.VAR_INT, CreateBalloonPacket::balloonAge,
        CreateBalloonPacket::new
    );

    @Override
    public @NotNull PacketDefinition<? extends NetworkPacket, ? extends ByteBuf> getDefinition() {
        return TBPackets.CREATE_BALLOON;
    }

    public int getBalloonAge() {
        if (this.balloonAge() == -1) {
            return TalkBalloons.config.balloonAge * 20;
        }

        return this.balloonAge();
    }
}
