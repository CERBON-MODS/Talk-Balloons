package com.cerbon.talk_balloons.network.packets;

import net.minecraft.resources.ResourceLocation;

public record SyncBalloonConfigPacket(
    int textColor,
    int balloonColor,
    ResourceLocation balloonTexture
) {
}
