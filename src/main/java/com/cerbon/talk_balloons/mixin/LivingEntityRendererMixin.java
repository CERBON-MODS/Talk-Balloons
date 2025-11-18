package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >= 1.21.3
/*import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;*/
//? if >= 1.21.9 {
/*import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
*///?} else if >= 1.21.3 {
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?}

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
//? if >= 1.21.3 {
/*public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    //? if >= 1.21.9 {
    /^@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    private void tb_tryRenderBalloons(LivingEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!(renderState instanceof AvatarRenderState playerRenderState))
            return;

        if (playerRenderState.isInvisible)
            return;

        IPlayerRenderState stateMixin = (IPlayerRenderState) renderState;

        if (stateMixin.tb_getBalloons() == null)
            return;

        BalloonRenderer.renderBalloons(poseStack, null, BalloonRenderer.toEulerXyzDegrees(cameraRenderState.orientation), this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight + 0.3f, stateMixin.tb_getPlayerConfigData());
    }
    ^///?} else {
    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void tb_tryRenderBalloons(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (!(renderState instanceof PlayerRenderState playerRenderState))
            return;

        if (playerRenderState.isInvisible)
            return;

        IPlayerRenderState stateMixin = (IPlayerRenderState) renderState;

        if (stateMixin.tb_getBalloons() == null)
            return;

        BalloonRenderer.renderBalloons(poseStack, bufferSource, BalloonRenderer.toEulerXyzDegrees(this.entityRenderDispatcher.cameraOrientation()), this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight + 0.3f, stateMixin.tb_getPlayerConfigData());
    }
    //?}
}
*///?} else {

public abstract class LivingEntityRendererMixin {
}
//?}