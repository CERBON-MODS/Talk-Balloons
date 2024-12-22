package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.client.BalloonRenderer;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float shadowRadius) {
        super(context, entityModel, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void tb_render(AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (player.isInvisible() || !player.isAlive()) return;

        IAbstractClientPlayer playerMixin = (IAbstractClientPlayer) player;
        if (playerMixin.getBalloonMessages() == null || playerMixin.getBalloonMessages().isEmpty()) return;

        BalloonRenderer.renderBalloons(poseStack, this.entityRenderDispatcher, this.getFont(), playerMixin.getBalloonMessages(), player.getBbHeight());
    }
}
