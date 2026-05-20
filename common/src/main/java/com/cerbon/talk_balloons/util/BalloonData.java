package com.cerbon.talk_balloons.util;

import net.minecraft.network.chat.Component;

public record BalloonData(
    Component text,
    long creationTime,
    int ticksToLive
) {
    public static BalloonData create(Component text, int ticksToLive) {
        return new BalloonData(text, System.currentTimeMillis(), ticksToLive);
    }
}
