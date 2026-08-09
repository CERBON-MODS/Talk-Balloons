//? if <= 1.20.1 {
package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager;
import com.cerbon.talk_balloons.util.mixin.IGuiSectionProvider;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.server.packs.resources.Resource;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {
    @ModifyReturnValue(method = "loadSprite", at = @At("RETURN"))
    private static SpriteContents tryLoadGuiSpriteMetadata(SpriteContents original, @Local(argsOnly = true) Resource resource) {
        try {
            var guiSection = resource.metadata().getSection(BalloonSpriteManager.SECTION_SERIALIZER);
            if (guiSection.isPresent()) {
                ((IGuiSectionProvider) original).talk_balloons$setSection(guiSection.orElseThrow());
            }
        } catch (Throwable ignored) {
        }

        return original;
    }
}
//? }
