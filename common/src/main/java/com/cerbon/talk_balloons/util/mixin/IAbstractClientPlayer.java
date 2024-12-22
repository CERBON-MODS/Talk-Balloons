package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;

public interface IAbstractClientPlayer {
    void createBalloonMessage(String text, int duration);
    HistoricalData<String> getBalloonMessages();
}
