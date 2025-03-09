package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Mixin(Player.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public abstract class PlayerMixin extends LivingEntity implements IAbstractClientPlayer {
    @Unique private HistoricalData<String> balloonMessages;
    @Unique private Set<Supplier<Boolean>> talk_balloons$queuedTickEvents = new HashSet<>();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
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

    @Environment(EnvType.CLIENT)
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickQueuedEvents(CallbackInfo ci) {
        if (!((Object) this instanceof AbstractClientPlayer))
            return;

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
