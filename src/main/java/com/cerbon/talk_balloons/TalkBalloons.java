package com.cerbon.talk_balloons;

import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class TalkBalloons {
	public static TBConfig config;
	private static final Map<UUID, SynchronizedConfigData> SYNCHRONIZED_CONFIG_DATAS = Collections.synchronizedMap(new HashMap<>());

	public static void init() {
		AutoConfig.register(TBConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TBConfig.class).get();
	}

	public static ResourceLocation id(String path) {
		//? if < 1.21 {
		return new ResourceLocation(TBConstants.MOD_ID, path);
		//?} else {
		/*return ResourceLocation.fromNamespaceAndPath(TBConstants.MOD_ID, path);
		*///?}
	}

	public static SynchronizedConfigData getPlayerConfig(Player player) {
		return SYNCHRONIZED_CONFIG_DATAS.get(player.getUUID());
	}

	public static void setPlayerConfig(Player player, SynchronizedConfigData configData) {
		SYNCHRONIZED_CONFIG_DATAS.put(player.getUUID(), configData);
	}
}
