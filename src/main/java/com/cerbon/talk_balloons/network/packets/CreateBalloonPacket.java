package com.cerbon.talk_balloons.network.packets;

import net.minecraft.network.chat.Component;

public record CreateBalloonPacket(
    Component message,
    int balloonAge
) {
}
