package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
//? if >= 1.21.3
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;*/
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >= 1.21.3
/*@Mixin(PlayerRenderState.class)*/
public abstract class PlayerRenderStateMixin implements IPlayerRenderState {
    @Unique private HistoricalData<Component> tb_balloons;

    @Override
    public HistoricalData<Component> tb_getBalloons() {
        return tb_balloons;
    }

    @Override
    public void tb_setBalloons(HistoricalData<Component> balloons) {
        this.tb_balloons = balloons;
    }
}