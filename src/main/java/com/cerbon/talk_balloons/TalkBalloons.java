package com.cerbon.talk_balloons;

import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.network.packets.TalkBalloonsStatusPacket;
import com.cerbon.talk_balloons.util.SyncedConfigManager;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xyz.bluspring.modernnetworking.api.minecraft.VanillaPacketSender;

import java.util.*;

public class TalkBalloons {
	public static TBConfig config;
	public static final SyncedConfigManager serverSyncedConfigs = new SyncedConfigManager();
	private static final Set<UUID> playersWithSupport = Collections.synchronizedSet(new HashSet<>());

	public static void init() {
		AutoConfig.register(TBConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TBConfig.class).get();

		TBPackets.init();
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

	public static ResourceLocation id(String path) {
		//? if < 1.21 {
		return new ResourceLocation(TBConstants.MOD_ID, path);
		//?} else {
		/*return ResourceLocation.fromNamespaceAndPath(TBConstants.MOD_ID, path);
		*///?}
	}
}
