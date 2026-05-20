package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.BalloonData;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;
import com.cerbon.talk_balloons.util.mixin.IPlayerRenderState;
//? if >= 1.21.9 {
/*import net.minecraft.client.renderer.entity.state.AvatarRenderState;
*///?} else if >= 1.21.3 {
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?}

//? if >= 1.21.3 {
/*import org.spongepowered.asm.mixin.Mixin;
*///? }
import org.spongepowered.asm.mixin.Unique;

//? if >= 1.21.9 {
/*@Mixin(AvatarRenderState.class)
*///?} else if >= 1.21.3 {
/*@Mixin(PlayerRenderState.class)
*///?}
public abstract class PlayerRenderStateMixin implements IPlayerRenderState {
    @Unique private HistoricalData<BalloonData> tb_balloons;
    @Unique private SynchronizedConfigData tb_configData;

    @Override
    public HistoricalData<BalloonData> tb_getBalloons() {
        return tb_balloons;
    }

    @Override
    public void tb_setBalloons(HistoricalData<BalloonData> balloons) {
        this.tb_balloons = balloons;
    }

    @Override
    public SynchronizedConfigData tb_getPlayerConfigData() {
        return tb_configData;
    }

    @Override
    public void tb_setPlayerConfigData(SynchronizedConfigData configData) {
        this.tb_configData = configData;
    }
}
