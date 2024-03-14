package com.cerbon.talk_balloons.voicechat;

import com.cerbon.talk_balloons.util.AudioUtils;
import com.cerbon.talk_balloons.util.TBConstants;
import com.cerbon.talk_balloons.voicechat.whisper.WhisperHandler;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;

import java.util.List;

@ForgeVoicechatPlugin
public class TBVoiceChatPlugin implements VoicechatPlugin {
    private static final int SAMPLE_RATE = 48000; // Replace with your actual sample rate
    private static final int CHUNK_SIZE = 5 * SAMPLE_RATE;

    private final short[] buffer = new short[CHUNK_SIZE];
    private int bufferIndex = 0;

    @Override
    public String getPluginId() {
        return TBConstants.MOD_ID;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(ClientReceiveSoundEvent.EntitySound.class, this::onClientReceiveSoundEvent);
    }

    private void onClientReceiveSoundEvent(ClientReceiveSoundEvent.EntitySound event) {
        short[] audioData = event.getRawAudio();

        for (short sample : audioData) {
            buffer[bufferIndex] = sample;
            bufferIndex++;

            if (bufferIndex == CHUNK_SIZE) {
                processChunk(buffer, event.getVoicechat());
                bufferIndex = 0;
            }
        }
    }

    private void processChunk(short[] micAudioChunk, VoicechatApi voicechatApi) {
        float[] samples = AudioUtils.convertToWhisperFormat(voicechatApi.getAudioConverter().shortsToBytes(micAudioChunk));

        WhisperHandler.transcribeAudioToText(samples);

        List<String> transcribedText =  WhisperHandler.getTranscribedText();
        for (String text : transcribedText)
            System.out.println(text);
    }
}
