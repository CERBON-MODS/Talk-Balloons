package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.util.TBConstants;
import io.netty.buffer.ByteBuf;
//? if >= 1.20.6
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class TBPackets {
    public static final int PROTOCOL_VERSION = 1;

    // Dual (C <-> S) packets
    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, TalkBalloonsStatusPacket> STATUS = register("status", TalkBalloonsStatusPacket.CODEC);

    // Serverbound (C -> S) packets
    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, SyncBalloonConfigPacket> SYNC_BALLOON_CONFIG = register("sync_balloon_config", SyncBalloonConfigPacket.CODEC);

    // Clientbound (S -> C) packets
    public static final CustomPacketPayload.TypeAndCodec</*? if >= 1.20.6 {*/RegistryFriendlyByteBuf/*?} else {*//*FriendlyByteBuf*//*?}*/, CreateBalloonPacket> CREATE_BALLOON = register("create_balloon", CreateBalloonPacket.CODEC);
    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, SyncBalloonConfigToPlayerPacket> SYNC_CONFIG_TO_PLAYER = register("sync_balloon_config", SyncBalloonConfigToPlayerPacket.CODEC);

    private static <B extends FriendlyByteBuf, V extends CustomPacketPayload> CustomPacketPayload.TypeAndCodec<B, V> register(String path, StreamCodec<B, V> codec) {
        return new CustomPacketPayload.TypeAndCodec<>(new CustomPacketPayload.Type<>(TalkBalloons.id(path)), codec);
    }

    public static void init() {
    }
}
