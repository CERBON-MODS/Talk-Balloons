package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.network.FriendlyByteBuf;
import xyz.bluspring.modernnetworking.api.PacketDefinition;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaNetworkRegistry;

public class TBPackets {
    public static final int PROTOCOL_VERSION = 1;
    public static final VanillaNetworkRegistry REGISTRY = VanillaNetworkRegistry.create(TBConstants.MOD_ID);

    // Dual (C <-> S) packets
    public static final PacketDefinition<TalkBalloonsStatusPacket, FriendlyByteBuf> STATUS = REGISTRY.registerDual("status", TalkBalloonsStatusPacket.CODEC);

    // Serverbound (C -> S) packets
    public static final PacketDefinition<SyncBalloonConfigPacket, FriendlyByteBuf> SYNC_BALLOON_CONFIG = REGISTRY.registerServerbound("sync_balloon_config", SyncBalloonConfigPacket.CODEC);

    // Clientbound (S -> C) packets
    public static final PacketDefinition<CreateBalloonPacket, FriendlyByteBuf> CREATE_BALLOON = REGISTRY.registerClientbound("create_balloon", CreateBalloonPacket.CODEC);
    public static final PacketDefinition<SyncBalloonConfigToPlayerPacket, FriendlyByteBuf> SYNC_CONFIG_TO_PLAYER = REGISTRY.registerClientbound("sync_balloon_config", SyncBalloonConfigToPlayerPacket.CODEC);

    public static void init() {
        try {
            TBClientPacketHandler.init();
        } catch (Throwable ignored) {
            // this likely means we're on dedicated server, ignore.
        }

        TBServerPacketHandler.init();
    }
}
