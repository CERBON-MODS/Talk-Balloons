package com.cerbon.talk_balloons.config;

import com.cerbon.talk_balloons.TalkBalloons;
import net.minecraft.resources.ResourceLocation;

public enum BalloonStyle {
    SQUARED(TalkBalloons.id("textures/gui/squared.png")),
    ROUNDED(TalkBalloons.id("textures/gui/rounded.png")),
    CIRCULAR(TalkBalloons.id("textures/gui/circular.png"));
    // TODO: support custom balloons?

    private final ResourceLocation textureId;

    BalloonStyle(ResourceLocation textureId) {
        this.textureId = textureId;
    }

    public ResourceLocation getTextureId() {
        return this.textureId;
    }
}
