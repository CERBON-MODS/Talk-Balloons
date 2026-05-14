package com.cerbon.talk_balloons.network;

import com.cerbon.talk_balloons.api.TalkBalloonsApi;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import com.cerbon.talk_balloons.network.packets.CreateBalloonPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigPacket;
import com.cerbon.talk_balloons.network.packets.SyncBalloonConfigToPlayerPacket;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;

import net.minecraft.client.Minecraft;

public class TBClientPacketHandler {
    public static void handleCreateBalloon(CreateBalloonPacket packet) {
        var level = Minecraft.getInstance().level;

        if (level == null)
            return;

        var player = level.getPlayerByUUID(packet.uuid());

        if (player == null)
            return;

        TalkBalloonsApi.INSTANCE.createBalloonMessage(player, packet.message(), packet.getBalloonAge());
    }

    public static void handleStatus(TalkBalloonsStatusPacket packet) {
        if (packet.protocolVersion() <= TBPackets.PROTOCOL_VERSION) {
            TalkBalloonsClient.enableServerSupport();
            VanillaClientPacketSender.sendToServer(new TalkBalloonsStatusPacket(TBPackets.PROTOCOL_VERSION));
            syncBalloonConfig();
        }
    }

    public static void handleSyncConfigToPlayer(SyncBalloonConfigToPlayerPacket packet) {
        TalkBalloonsClient.syncedConfigs.setPlayerConfig(packet.uuid(), packet.data());
    }

    public static void syncBalloonConfig() {
        TalkBalloonsClient.syncedConfigs.resetDefault();
        if (TalkBalloonsClient.hasServerSupport())
            VanillaClientPacketSender.sendToServer(new SyncBalloonConfigPacket(SynchronizedConfigData.getDefault()));
    }
}
