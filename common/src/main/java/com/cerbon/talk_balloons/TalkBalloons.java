package com.cerbon.talk_balloons;

import com.cerbon.talk_balloons.config.TBConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class TalkBalloons {
	public static TBConfig config;

	public static void init() {
		AutoConfig.register(TBConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TBConfig.class).get();
	}
}
