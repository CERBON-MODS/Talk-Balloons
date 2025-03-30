package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Mixin(AbstractClientPlayer.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public abstract class AbstractClientPlayerMixin extends Player implements IAbstractClientPlayer {
    @Unique private HistoricalData<String> balloonMessages;
    @Unique private final Collection<Supplier<Boolean>> talk_balloons$queuedTickEvents = new ConcurrentLinkedDeque<>();

    public AbstractClientPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Override
    public void createBalloonMessage(String text, int timeToRemove) {
        if (balloonMessages == null)
            balloonMessages = new HistoricalData<>(text, TalkBalloons.config.maxBalloons);
        else
            balloonMessages.add(text);

        var currentTick = new AtomicInteger(0);
        talk_balloons$queuedTickEvents.add(() -> {
            if (currentTick.getAndIncrement() >= timeToRemove) {
                balloonMessages.remove(text);
                return true;
            }

            return false;
        });
    }

    @Override
    public HistoricalData<String> getBalloonMessages() {
        return balloonMessages;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickQueuedEvents(CallbackInfo ci) {
        var eventsToRemove = new HashSet<Supplier<Boolean>>();
        for (Supplier<Boolean> event : talk_balloons$queuedTickEvents) {
            if (event.get()) {
                eventsToRemove.add(event);
            }
        }

        if (!eventsToRemove.isEmpty()) {
            talk_balloons$queuedTickEvents.removeAll(eventsToRemove);
        }
    }
}
