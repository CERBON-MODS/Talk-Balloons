package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusClientPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusServerPacket;
import com.cerbon.talk_balloons.util.TBConstants;
import xyz.bluspring.modernnetworking.api.v2.codec.NetworkCodec;
import xyz.bluspring.modernnetworking.api.v2.packet.NetworkPacket;
import xyz.bluspring.modernnetworking.api.v2.packet.PacketDefinition;
import xyz.bluspring.modernnetworking.api.v2.packet.registry.NamespacedPacketRegistry;
import xyz.bluspring.modernnetworking.minecraft.api.v2.packet.MinecraftPacketRegistries;
//? if >= 1.20.6
//import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class TBPackets {
    public static final int PROTOCOL_VERSION = 2;
    private static final NamespacedPacketRegistry SERVER_REGISTRY = MinecraftPacketRegistries.SERVER_PLAY.namespaced(TBConstants.MOD_ID);
    private static final NamespacedPacketRegistry CLIENT_REGISTRY = MinecraftPacketRegistries.CLIENT_PLAY.namespaced(TBConstants.MOD_ID);

    // Dual (C <-> S) packets
    public static final PacketDefinition<FriendlyByteBuf, TalkBalloonsStatusServerPacket> STATUS_SERVER = SERVER_REGISTRY.register("status/server", TalkBalloonsStatusServerPacket.CODEC);
    public static final PacketDefinition<FriendlyByteBuf, TalkBalloonsStatusClientPacket> STATUS_CLIENT = CLIENT_REGISTRY.register("status/client", TalkBalloonsStatusClientPacket.CODEC);

    // Serverbound (C -> S) packets
    public static final PacketDefinition<FriendlyByteBuf, SyncBalloonConfigPacket> SYNC_BALLOON_CONFIG = SERVER_REGISTRY.register("sync_balloon_config", SyncBalloonConfigPacket.CODEC);

    // Clientbound (S -> C) packets
    public static final PacketDefinition</*? if >= 1.20.6 {*//*RegistryFriendlyByteBuf*//*?} else {*/FriendlyByteBuf/*?}*/, CreateBalloonPacket> CREATE_BALLOON = CLIENT_REGISTRY.register("create_balloon", CreateBalloonPacket.CODEC);
    public static final PacketDefinition<FriendlyByteBuf, SyncBalloonConfigToPlayerPacket> SYNC_CONFIG_TO_PLAYER = CLIENT_REGISTRY.register("sync_config_to_player", SyncBalloonConfigToPlayerPacket.CODEC);

    private static <B extends FriendlyByteBuf, V extends NetworkPacket> PacketDefinition<B, V> registerDual(String path, NetworkCodec<B, V> codec) {
        return SERVER_REGISTRY.register(CLIENT_REGISTRY.register(path, codec));
    }

    public static void init() {
        TBServerPacketHandler.init();
        TBClientPacketHandler.init();
    }
}
