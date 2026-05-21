package com.cerbon.talk_balloons.mixin;

//? if < 1.21.11 {
//?}

//? if >= 1.21.9 {
/*import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.cerbon.talk_balloons.util.mixin.ICameraRenderState;

//? if <= 1.21.11 {
import net.minecraft.client.renderer.state.CameraRenderState;
//? } else {
/^import net.minecraft.client.renderer.state.level.CameraRenderState;
^///? }

*///?} else if >= 1.21.3 {
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
*///? }

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

//? if >= 1.21.3 {
/*import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Mixin(LivingEntityRenderer.class)
//? if >= 1.21.3 {
/*public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    //? if >= 1.21.9 {
    /^@Inject(method = {
        //? if <= 1.21.11 {
        "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"
        //? } else {
        /^¹"submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
        ¹^///? }
    }, at = @At("HEAD"))
    private void tb_tryRenderBalloons(LivingEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!(renderState instanceof AvatarRenderState playerRenderState))
            return;

        if (playerRenderState.isInvisible)
            return;

        IPlayerRenderState stateMixin = (IPlayerRenderState) renderState;

        if (stateMixin.tb_getBalloons() == null)
            return;

        BalloonRenderer.submitBalloons(poseStack, ((ICameraRenderState) cameraRenderState).talk_balloons$yaw(), this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight, playerRenderState.isCrouching, stateMixin.tb_getPlayerConfigData(), renderState.lightCoords);
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

        BalloonRenderer.submitBalloons(poseStack, this.entityRenderDispatcher.camera.getYRot(), this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight, playerRenderState.isCrouching, stateMixin.tb_getPlayerConfigData(), packedLight);
    }
    //?}
}
*///?} else {

public abstract class LivingEntityRendererMixin {
}
//?}
