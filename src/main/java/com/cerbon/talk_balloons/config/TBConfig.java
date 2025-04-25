package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.util.TBConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Optional;

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

    @ColorSelector
    public int textColor = 0x141414; // RGB-encoded
    @ColorSelector
    public int balloonTint = 0xF1F6F8; // RGB-encoded

    public boolean showOwnBalloon = true;

    public static class ConfigGuiHandler {
        public static void init() {
            var registry = AutoConfig.getGuiRegistry(TBConfig.class);
            registry.registerAnnotationProvider((key, field, config, defaults, $) -> {
                try {
                    return Collections.singletonList(new ColorEntry(Component.translatable(key),
                        (int) field.get(config),
                        Component.empty(),
                        () -> (int) defaults,
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
