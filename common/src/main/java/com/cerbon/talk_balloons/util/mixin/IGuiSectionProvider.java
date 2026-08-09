package com.cerbon.talk_balloons.util.mixin;

//? if <= 1.20.1 {
import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager;
//?}

public interface IGuiSectionProvider {
    //? if <= 1.20.1 {
    BalloonSpriteManager.GuiMetadataSection talk_balloons$getSection();
    void talk_balloons$setSection(BalloonSpriteManager.GuiMetadataSection section);
    //? }
}
