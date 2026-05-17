package com.cerbon.talk_balloons.mixin;

//? if >= 1.21.10 {
/*import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cerbon.talk_balloons.client.resources.BalloonStyle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
import net.minecraft.client.resources.model.AtlasManager;

@Mixin(AtlasManager.class)
*///? }
public abstract class AtlasManagerMixin {
    //? if >= 1.21.10 {
    /*@Shadow @Final @Mutable private static List<AtlasManager.AtlasConfig> KNOWN_ATLASES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void appendCustomBalloonAtlas(CallbackInfo ci) {
        var atlases = new ArrayList<>(KNOWN_ATLASES);
        atlases.add(new AtlasManager.AtlasConfig(BalloonStyle.BALLOONS_SHEET, BalloonStyle.BALLOONS_ATLAS, false, Set.of(GuiMetadataSection.TYPE)));
        KNOWN_ATLASES = atlases;
    }
    *///? }
}
