package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import net.minecraft.network.chat.Component;
//? if < 1.19 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///?}

import java.util.Collections;
import java.util.Optional;

@Config(name = TBConstants.MOD_ID)
public class TBConfig implements ConfigData {

    public float balloonsHeightOffset = 0.9f;
    public int distanceBetweenBalloons = 3;

    @ConfigEntry.BoundedDiscrete(max = 16, min = 1)
    public int maxBalloons = 7;
    @ConfigEntry.BoundedDiscrete(max = 512, min = 0)
    public int minBalloonWidth = 13;
    @ConfigEntry.BoundedDiscrete(max = 512, min = 0)
    public int maxBalloonWidth = 180;

    @ConfigEntry.BoundedDiscrete(max = 64, min = 0)
    public int balloonPadding = 1;

    @Comment("In seconds")
    @ConfigEntry.BoundedDiscrete(max = 120, min = 0)
    public int balloonAge = 15;

    public BalloonStyle balloonStyle = BalloonStyle.ROUNDED;

    @ColorSelector
    public int textColor = 0x141414; // RGB-encoded
    @ColorSelector
    public int balloonTint = 0xF1F6F8; // RGB-encoded

    public boolean showOwnBalloon = true;
    public boolean onlyDisplayBalloons = false;

    public static class ConfigGuiHandler {
        public static void init() {
            var registry = AutoConfig.getGuiRegistry(TBConfig.class);
            registry.registerAnnotationProvider((key, field, config, defaults, $) -> {
                try {
                    return Collections.singletonList(new ColorEntry(
                        //? if < 1.19 {
                        /*new TranslatableComponent(key),
                        *///?} else {
                        Component.translatable(key),
                        //?}
                        field.getInt(config),
                        //? if < 1.19 {
                        /*new TranslatableComponent("text.cloth-config.reset_value"),
                        *///?} else {
                        Component.translatable("text.cloth-config.reset_value"),
                        //?}
                        () -> {
                            try {
                                return field.getInt(defaults);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        newValue -> {
                            try {
                                field.set(config, newValue);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        Optional::empty,
                        false
                    ));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }, ColorSelector.class);
        }
    }
}
