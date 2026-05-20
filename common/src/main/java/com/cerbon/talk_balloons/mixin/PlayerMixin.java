package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.BalloonData;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.mixin.ITalkBalloonsPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements ITalkBalloonsPlayer {
    @Unique private HistoricalData<BalloonData> talk_balloons$balloonMessages;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private void talk_balloons$tryInitHistoricalData() {
        if (talk_balloons$balloonMessages == null || talk_balloons$balloonMessages.getMaxHistory() != TalkBalloons.config.getMaxBalloons())
            talk_balloons$balloonMessages = new HistoricalData<>(TalkBalloons.config.getMaxBalloons());
    }

    @Override
    public void talk_balloons$createBalloonMessage(Component text, int timeToRemove) {
        talk_balloons$tryInitHistoricalData();

        var balloonMessages = this.talk_balloons$getBalloonMessages();
        balloonMessages.add(BalloonData.create(text, timeToRemove));
    }

    @Override
    public HistoricalData<BalloonData> talk_balloons$getBalloonMessages() {
        talk_balloons$tryInitHistoricalData();

        return talk_balloons$balloonMessages;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickQueuedEvents(CallbackInfo ci) {
        if (this.talk_balloons$balloonMessages == null)
            return;

        List<BalloonData> dataToRemove = null;
        long currentTime = System.currentTimeMillis();

        for (BalloonData message : this.talk_balloons$balloonMessages) {
            if (message.ticksToLive() > 0) {
                int msToLive = message.ticksToLive() * 50;
                if (currentTime - message.creationTime() >= msToLive) {
                    if (dataToRemove == null)
                        dataToRemove = new ArrayList<>();

                    dataToRemove.add(message);
                }
            }
        }

        if (dataToRemove != null)
            this.talk_balloons$balloonMessages.removeAll(dataToRemove);
    }
}
