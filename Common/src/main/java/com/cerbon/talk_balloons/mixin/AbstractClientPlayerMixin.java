package com.cerbon.talk_balloons.mixin;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.api.general.event.TimedEvent;
import com.cerbon.cerbons_api.api.static_utilities.CapabilityUtils;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public abstract class AbstractClientPlayerMixin extends Player implements IAbstractClientPlayer {
    @Unique private HistoricalData<String> balloonMessages;

    public AbstractClientPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Override
    public void createBalloonMessage(String text, int timeToRemove) {
        if (balloonMessages == null)
            balloonMessages = new HistoricalData<>(text, 7);
        else
            balloonMessages.add(text);

        CapabilityUtils.getLevelEventScheduler(this.level()).addEvent(
                new TimedEvent(
                        () -> balloonMessages.remove(text),
                        timeToRemove
                )
        );
    }

    @Override
    public HistoricalData<String> getBalloonMessages() {
        return balloonMessages;
    }
}
