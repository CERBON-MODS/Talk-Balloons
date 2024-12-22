package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {
    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("TAIL"))
    private void tb_setupBalloonRenderState(AbstractClientPlayer player, PlayerRenderState reusedState, float partialTick, CallbackInfo ci) {
        IAbstractClientPlayer playerMixin = (IAbstractClientPlayer) player;
        IPlayerRenderState stateMixin = (IPlayerRenderState) reusedState;
        if (playerMixin.getBalloonMessages() == null || playerMixin.getBalloonMessages().isEmpty()) {
            stateMixin.tb_setBalloons(null);
            return;
        }

        ((IPlayerRenderState) reusedState).tb_setBalloons(playerMixin.getBalloonMessages());
    }
}
