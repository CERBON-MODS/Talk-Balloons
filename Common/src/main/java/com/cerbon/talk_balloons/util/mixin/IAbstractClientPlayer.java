package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;

public interface IAbstractClientPlayer {
    void createBalloonMessage(String text, int duration);
    HistoricalData<String> getBalloonMessages();
}
