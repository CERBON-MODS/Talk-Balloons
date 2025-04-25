package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import net.minecraft.network.chat.Component;

public interface IPlayerRenderState {
    HistoricalData<Component> tb_getBalloons();
    void tb_setBalloons(HistoricalData<Component> balloons);

    SynchronizedConfigData tb_getPlayerConfigData();
    void tb_setPlayerConfigData(SynchronizedConfigData configData);
}
