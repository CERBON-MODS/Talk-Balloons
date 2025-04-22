package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.resources.ResourceLocation;

@Config(name = TBConstants.MOD_ID)
public class TBConfig implements ConfigData {

    public float balloonsHeightOffset = 0.9f;
    public int distanceBetweenBalloons = 3;

    public int maxBalloons = 7;
    public int minBalloonWidth = 13;
    public int maxBalloonWidth = 180;

    public int balloonPadding = 1;

    @Comment("In seconds")
    public int balloonAge = 15;

    public BalloonStyle balloonStyle = BalloonStyle.ROUNDED;

    public int textColor = 1315860;

    public boolean showOwnBalloon = true;
}
