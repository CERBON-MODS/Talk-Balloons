package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.talk_balloons.util.BalloonData;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.config.SynchronizedConfigData;
import net.minecraft.network.chat.Component;

public interface IPlayerRenderState {
    HistoricalData<BalloonData> tb_getBalloons();
    void tb_setBalloons(HistoricalData<BalloonData> balloons);

    SynchronizedConfigData tb_getPlayerConfigData();
    void tb_setPlayerConfigData(SynchronizedConfigData configData);
}
