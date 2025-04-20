package com.cerbon.talk_balloons;

import com.cerbon.talk_balloons.config.TBConfig;
import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;

public class TalkBalloons {
	public static TBConfig config;

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
}
