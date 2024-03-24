package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = TBConstants.MOD_ID)
public class TBConfig implements ConfigData {

    public float balloonHeightOffset = 0.9f;

    public int maxBalloons = 7;
    @Comment("In seconds")
    public int balloonAge = 15;

    public int textColor = 1315860;

    public boolean showOwnBalloon = true;
}
