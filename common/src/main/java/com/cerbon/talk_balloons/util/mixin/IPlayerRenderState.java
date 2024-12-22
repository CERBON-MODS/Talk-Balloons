package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;

public interface IPlayerRenderState {
    HistoricalData<String> tb_getBalloons();
    void tb_setBalloons(HistoricalData<String> balloons);
}
