package com.cerbon.talk_balloons.mixin;

//? if >= 1.21.8 {
/*import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.client.config.GuiBalloonRenderer;
import com.mojang.blaze3d.ProjectionType;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.render.GuiRenderer;

//? if <= 1.21.8 {
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
//? }

@Mixin(GuiRenderer.class)
*///? }
public abstract class GuiRendererMixin {
    //? if >= 1.21.6 && <= 1.21.8 {
    /*@Shadow @Final
    private CachedOrthoProjectionMatrixBuffer guiProjectionMatrixBuffer;
    *///? }

    //? if >= 1.21.8 {
    /*@Inject(method = "draw", at = @At("TAIL"))
    private void renderBalloonsInGui(CallbackInfo ci) {
        //? if <= 1.21.8 {
        // This needs to be done on 1.21.6-1.21.8 specifically for some reason. It works fine on newer versions.
        var window = Minecraft.getInstance().getWindow();
        var oldProjMat = RenderSystem.getProjectionMatrixBuffer();
        var oldProjType = RenderSystem.getProjectionType();
        RenderSystem.setProjectionMatrix(this.guiProjectionMatrixBuffer.getBuffer((float)window.getWidth() / (float)window.getGuiScale(), (float)window.getHeight() / (float)window.getGuiScale()), ProjectionType.ORTHOGRAPHIC);
        RenderSystem.getDynamicUniforms().writeTransform((new Matrix4f()).setTranslation(0.0F, 0.0F, -11000.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f(), 0.0F);
        //? }

        BalloonRenderer.renderBalloons(GuiBalloonRenderer.getRenderQueue());

        //? if <= 1.21.8 {
        RenderSystem.setProjectionMatrix(oldProjMat, oldProjType);
        RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f(), new Vector4f(1.0F), new Vector3f(), new Matrix4f(), 0.0F);
        //? }
    }
    *///? }
}
