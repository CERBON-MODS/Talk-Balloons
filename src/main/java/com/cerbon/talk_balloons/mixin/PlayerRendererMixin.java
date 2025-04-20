package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
//? if <= 1.21.1 {
import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//?} else if >= 1.21.3 {
/*import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<
    AbstractClientPlayer,
    /*? if >= 1.21.3 {*//*PlayerRenderState,
    *//*?}*/ PlayerModel/*? if < 1.21.3 {*/<AbstractClientPlayer>/*?}*/
> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel/*? if < 1.21.3 {*/<AbstractClientPlayer>/*?}*/ model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    //? if <= 1.21.1 {
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void tb_render(AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (player.isInvisible() || !player.isAlive()) return;

        ITalkBalloonsPlayer playerMixin = (ITalkBalloonsPlayer) player;
        if (playerMixin.talk_balloons$getBalloonMessages() == null || playerMixin.talk_balloons$getBalloonMessages().isEmpty()) return;

        BalloonRenderer.renderBalloons(poseStack, this.entityRenderDispatcher, this.getFont(), playerMixin.talk_balloons$getBalloonMessages(), player.getBbHeight());
    }
    //?} else if >= 1.21.3 {

    /*@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("TAIL"))
    private void tb_setupBalloonRenderState(AbstractClientPlayer player, PlayerRenderState reusedState, float partialTick, CallbackInfo ci) {
        ITalkBalloonsPlayer playerMixin = (ITalkBalloonsPlayer) player;
        IPlayerRenderState stateMixin = (IPlayerRenderState) reusedState;
        if (playerMixin.talk_balloons$getBalloonMessages() == null || playerMixin.talk_balloons$getBalloonMessages().isEmpty()) {
            stateMixin.tb_setBalloons(null);
            return;
        }

        ((IPlayerRenderState) reusedState).tb_setBalloons(playerMixin.talk_balloons$getBalloonMessages());
    }
    *///?}
}
