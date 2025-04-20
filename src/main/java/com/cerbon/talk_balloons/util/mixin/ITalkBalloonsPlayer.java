package com.cerbon.talk_balloons.util.mixin;

import com.cerbon.talk_balloons.util.HistoricalData;
import net.minecraft.network.chat.Component;
//? if < 1.19 {
/*import net.minecraft.network.chat.TextComponent;
*///?}

public interface ITalkBalloonsPlayer {
    default void talk_balloons$createBalloonMessage(String text, int duration) {
        talk_balloons$createBalloonMessage(
            //? if >= 1.19 {
            Component.literal(text),
            //?} else {
            /*new TextComponent(text),
            *///?}
            duration
        );
    }

    void talk_balloons$createBalloonMessage(Component text, int duration);
    HistoricalData<Component> talk_balloons$getBalloonMessages();
}
