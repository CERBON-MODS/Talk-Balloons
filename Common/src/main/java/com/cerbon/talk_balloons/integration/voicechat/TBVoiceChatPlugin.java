package com.cerbon.talk_balloons.integration.voicechat;

import com.cerbon.talk_balloons.util.TBConstants;
import com.cerbon.talk_balloons.util.mixin.IAbstractClientPlayer;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

@ForgeVoicechatPlugin
public class TBVoiceChatPlugin implements VoicechatPlugin {
    private final Minecraft client = Minecraft.getInstance();

    @Override
    public String getPluginId() {
        return TBConstants.MOD_ID;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(ClientReceiveSoundEvent.EntitySound.class, this::onClientReceiveSoundEvent);
    }

    private void onClientReceiveSoundEvent(ClientReceiveSoundEvent.EntitySound event) {
        if (client.level == null) return;

        Player sender = client.level.getPlayerByUUID(event.getId());
        IAbstractClientPlayer senderMixin = (IAbstractClientPlayer) sender;
        if (sender == null) return;

        byte[] audioSample = event.getVoicechat().getAudioConverter().shortsToBytes(event.getRawAudio());

    }
}
