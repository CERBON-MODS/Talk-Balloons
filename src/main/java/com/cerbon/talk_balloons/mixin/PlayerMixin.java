package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Mixin(Player.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public abstract class PlayerMixin extends LivingEntity implements ITalkBalloonsPlayer {
    @Unique private HistoricalData<Component> talk_balloons$balloonMessages;
    @Unique private final Collection<Supplier<Boolean>> talk_balloons$queuedTickEvents = new ConcurrentLinkedDeque<>();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private void talk_balloons$tryInitHistoricalData() {
        if (talk_balloons$balloonMessages == null)
            talk_balloons$balloonMessages = new HistoricalData<>(TalkBalloons.config.maxBalloons);
    }

    @Override
    public void talk_balloons$createBalloonMessage(Component text, int timeToRemove) {
        talk_balloons$tryInitHistoricalData();

        var currentTick = new AtomicInteger(0);
        talk_balloons$queuedTickEvents.add(() -> {
            if (currentTick.getAndIncrement() >= timeToRemove) {
                talk_balloons$balloonMessages.remove(text);
                return true;
            }

            return false;
        });
    }

    @Override
    public HistoricalData<Component> talk_balloons$getBalloonMessages() {
        talk_balloons$tryInitHistoricalData();

        return talk_balloons$balloonMessages;
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