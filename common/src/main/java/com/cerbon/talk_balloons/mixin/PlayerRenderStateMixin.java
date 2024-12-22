package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerRenderState.class)
public abstract class PlayerRenderStateMixin implements IPlayerRenderState {
    @Unique private HistoricalData<String> tb_balloons;

    @Override
    public HistoricalData<String> tb_getBalloons() {
        return tb_balloons;
    }

    @Override
    public void tb_setBalloons(HistoricalData<String> balloons) {
        this.tb_balloons = balloons;
    }
}
