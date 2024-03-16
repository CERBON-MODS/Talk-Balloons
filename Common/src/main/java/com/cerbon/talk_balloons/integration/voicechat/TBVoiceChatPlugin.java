package com.cerbon.talk_balloons.integration.voicechat;

import com.cerbon.talk_balloons.util.AudioFormats;
import com.cerbon.talk_balloons.util.AudioUtils;
import com.cerbon.talk_balloons.util.TBConstants;
import com.cerbon.talk_balloons.util.vad.SileroVadDetector;
import com.cerbon.talk_balloons.util.vad.SileroVadHandler;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ForgeVoicechatPlugin
public class TBVoiceChatPlugin implements VoicechatPlugin {
    private final Map<UUID, List<byte[]>> buffer = new Object2ObjectOpenHashMap<>();

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

        byte[] audioSample48khz = event.getVoicechat().getAudioConverter().shortsToBytes(event.getRawAudio());
        byte[] audioSample16khz;
        try {
            audioSample16khz = AudioUtils.convertRawAudio(audioSample48khz, AudioFormats.PCM_48KHZ, AudioFormats.PCM_16KHZ);

        } catch (IOException e) {throw new RuntimeException(e);}

        if (audioSample16khz.length == 2) return; // End of transmission

        // Because one audio sample length is too short for the vad to detect (needs 32ms+) I created a buffer to have two audio samples instead of one.
        UUID senderUUID = event.getId();
        buffer.computeIfAbsent(senderUUID, k -> new ArrayList<>()).add(audioSample16khz);

        List<byte[]> audioSamples = buffer.get(senderUUID);

        if (audioSamples.size() == 2) {
            byte[] twoAudioSample = new byte[audioSample16khz.length * 2];

            int offset = 0;
            for (byte[] sample : audioSamples) {
                System.arraycopy(sample, 0, twoAudioSample, offset, sample.length);
                offset += sample.length;
            }

            SileroVadDetector vadDetector = SileroVadHandler.getVadDetector();

            Map<String, Double> detectResult = null;
            try {
                detectResult = vadDetector.apply(twoAudioSample, true);
            
            } catch (Exception e) {System.err.println("Error applying VAD detector: " + e.getMessage());}
            
            if (detectResult != null && !detectResult.isEmpty())
                System.out.println(detectResult);

            audioSamples.clear();
        }
    }
}
