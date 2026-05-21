package com.cerbon.talk_balloons;

import com.cerbon.talk_balloons.config.ITBConfig;
import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.config.TBConfigManager;
import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.network.VanillaPacketSender;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.config.SyncedConfigManager;
import com.cerbon.talk_balloons.util.TBConstants;
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation;
 //?} else {
/*import net.minecraft.resources.Identifier;
*///?}
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class TalkBalloons {
	public static ITBConfig config = TBConfig.INSTANCE;
	public static final SyncedConfigManager serverSyncedConfigs = new SyncedConfigManager();
	private static final Set<UUID> playersWithSupport = Collections.synchronizedSet(new HashSet<>());

	public static void init() {
		TBPackets.init();
        TBConfigManager.INSTANCE.getConfig().load();
	}

	public static void onPlayerJoin(ServerPlayer player) {
		VanillaPacketSender.sendToPlayer(player, new TalkBalloonsStatusPacket(TBPackets.PROTOCOL_VERSION));
	}

	public static void onPlayerDisconnect(UUID uuid) {
		serverSyncedConfigs.removePlayerConfig(uuid);
		playersWithSupport.remove(uuid);
	}

	public static void addSupportedPlayer(UUID uuid) {
		playersWithSupport.add(uuid);
	}

	public static boolean playerHasSupport(UUID uuid) {
		return playersWithSupport.contains(uuid);
	}

	public static /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/ id(String path) {
		//? if < 1.21 {
		/*return new ResourceLocation(TBConstants.MOD_ID, path);
		*///?} else {
		return /*? if < 1.21.11 {*/ResourceLocation/*?} else {*//*Identifier*//*?}*/.fromNamespaceAndPath(TBConstants.MOD_ID, path);
		//?}
	}
}
