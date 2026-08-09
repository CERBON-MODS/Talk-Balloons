//? if <= 1.20.1 {
package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager;
import com.cerbon.talk_balloons.util.mixin.IGuiSectionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.renderer.texture.SpriteContents;

@Mixin(SpriteContents.class)
public abstract class SpriteContentsMixin implements IGuiSectionProvider {
    @Unique private BalloonSpriteManager.GuiMetadataSection talk_balloons$guiSection = BalloonSpriteManager.GuiMetadataSection.EMPTY;

    @Override
    public BalloonSpriteManager.GuiMetadataSection talk_balloons$getSection() {
        return this.talk_balloons$guiSection;
    }

    @Override
    public void talk_balloons$setSection(BalloonSpriteManager.GuiMetadataSection section) {
        this.talk_balloons$guiSection = section;
    }
}
//? }
