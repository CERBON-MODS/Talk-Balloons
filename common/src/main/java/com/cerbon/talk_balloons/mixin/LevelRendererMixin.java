package com.cerbon.talk_balloons.mixin;

import org.spongepowered.asm.mixin.Mixin;
//? if >= 26.3 {
/*import com.cerbon.talk_balloons.client.BalloonRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    //? if >= 26.3 {
    /*@Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;close()V", shift = At.Shift.AFTER))
    private void talk_balloons$renderBalloonPass(CallbackInfo ci) {
        BalloonRenderer.renderBalloons();
    }
    *///? }
}
