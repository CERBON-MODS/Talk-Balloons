package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

public class TBClientPacketHandler {
    public static void init() {
        var registry = TBPackets.REGISTRY;

        registry.addClientboundHandler(TBPackets.CREATE_BALLOON, (packet, ctx) -> {
            TalkBalloonsApi.INSTANCE.createBalloonMessage(ctx.getPlayer(), packet.message(), packet.getBalloonAge());
        });

        registry.addClientboundHandler(TBPackets.STATUS, (packet, ctx) -> {
            if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
                TalkBalloonsClient.enableServerSupport();
                VanillaPacketSender.sendToServer(new TalkBalloonsStatusPacket(TBPackets.PROTOCOL_VERSION));
                syncBalloonConfig();
            }
        });

        registry.addClientboundHandler(TBPackets.SYNC_CONFIG_TO_PLAYER, (packet, ctx) -> {
            TalkBalloonsClient.syncedConfigs.setPlayerConfig(packet.uuid(), packet.data());
        });
    }

    public static void syncBalloonConfig() {
        TalkBalloonsClient.syncedConfigs.resetDefault();
        if (TalkBalloonsClient.hasServerSupport())
            VanillaPacketSender.sendToServer(new SyncBalloonConfigPacket(SynchronizedConfigData.getDefault()));
    }
}
