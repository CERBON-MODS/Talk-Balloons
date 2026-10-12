package com.cerbon.talk_balloons.mixin;

//? if >= 1.21.8 {
/*import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
*///? }

//? if >= 26.1 {
/*@Mixin(net.minecraft.client.gui.GuiGraphicsExtractor.class)
*///? } else if >= 1.21.8 {
/*@Mixin(net.minecraft.client.gui.GuiGraphics.class)
*///? }
public abstract class GuiGraphicsMixin {
    //? if >= 1.21.8 {
    /*@Inject(
        //? if >= 26.1 {
        /^method = "entity"
        ^///? } else {
        method = "submitEntityRenderState"
        //? }
        , at = @At("HEAD")
    )
    private void removeBalloonsInGui(CallbackInfo ci, @Local(argsOnly = true) EntityRenderState state) {
        if (state instanceof IPlayerRenderState playerState)
            playerState.tb_setBalloons(null);
    }
    *///? }
}
