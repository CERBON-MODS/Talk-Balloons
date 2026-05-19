package com.cerbon.talk_balloons.mixin;

//? if >= 1.21.8 {
/*import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.config.GuiBalloonRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.render.GuiRenderer;

//? if <= 1.21.8 {
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.ProjectionType;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import net.minecraft.client.Minecraft;
//? }

@Mixin(GuiRenderer.class)
*///? }
public abstract class GuiRendererMixin {
    //? if >= 1.21.8 {
    /*@Inject(method = "draw", at = @At("TAIL"))
    private void renderBalloonsInGui(CallbackInfo ci) {
        BalloonRenderer.renderBalloons(GuiBalloonRenderer.getRenderQueue());
    }
    *///? }
}
