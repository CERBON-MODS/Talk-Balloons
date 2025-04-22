package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.util.TBConstants;
import net.minecraft.network.FriendlyByteBuf;
import xyz.bluspring.modernnetworking.api.PacketDefinition;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaNetworkRegistry;

public class TBPackets {
    public static final VanillaNetworkRegistry REGISTRY = VanillaNetworkRegistry.create(TBConstants.MOD_ID);

    public static final PacketDefinition<CreateBalloonPacket, FriendlyByteBuf> CREATE_BALLOON = REGISTRY.registerClientbound("create_balloon", CreateBalloonPacket.CODEC);
    public static final PacketDefinition<SyncBalloonConfigPacket, FriendlyByteBuf> SYNC_BALLOON_CONFIG = REGISTRY.registerServerbound("sync_balloon_config", SyncBalloonConfigPacket.CODEC);
}
