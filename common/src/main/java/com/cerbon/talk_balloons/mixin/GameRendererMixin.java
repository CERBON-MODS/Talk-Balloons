package com.cerbon.talk_balloons.mixin;

import org.spongepowered.asm.mixin.Mixin;
//? if >= 1.21.10 {
/*import com.cerbon.talk_balloons.util.mixin.ICameraRenderState;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Camera;

//? if <= 1.21.11 {
import net.minecraft.client.renderer.state.CameraRenderState;
//? } else {
/^import net.minecraft.client.renderer.state.level.CameraRenderState;
^///? }

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    //? if >= 1.21.10 {
    /*@Shadow @Final private Camera mainCamera;

    @Inject(method = "extractCamera", at = @At("TAIL"))
    private void extractCameraYawData(CallbackInfo ci, @Local CameraRenderState renderState) {
        ((ICameraRenderState) renderState).talk_balloons$setYaw(this.mainCamera
            //? if <= 1.21.10 {
            .getYRot()
            //? } else {
            /^.yRot()
            ^///? }
        );
    }
    *///? }
}
