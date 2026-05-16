package com.cerbon.talk_balloons.mixin;

//? if < 1.21.11 {
//?}
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

    import org.spongepowered.asm.mixin.Mixin;

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

        BalloonRenderer.renderBalloons(poseStack, cameraRenderState.yRot, this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight + 0.3f, stateMixin.tb_getPlayerConfigData());
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

        BalloonRenderer.renderBalloons(poseStack, this.entityRenderDispatcher.camera.getYRot(), this.getFont(), stateMixin.tb_getBalloons(), playerRenderState.boundingBoxHeight + 0.3f, stateMixin.tb_getPlayerConfigData(), packedLight);
    }
    //?}
}
*///?} else {

public abstract class LivingEntityRendererMixin {
}
//?}
