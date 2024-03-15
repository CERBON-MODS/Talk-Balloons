package com.cerbon.talk_balloons.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class AudioUtils {

    public static byte[] convertRawAudio(byte[] audioData, AudioFormat originalFormat, AudioFormat targetFormat) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(audioData);

        AudioInputStream audioInputStream = new AudioInputStream(byteInputStream, originalFormat, audioData.length);
        AudioInputStream targetAudioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);

        return targetAudioInputStream.readAllBytes();
    }

    /**
     * Converts a byte array to a float array.
     * @param audioData The raw audio data in bytes. Must be in 16khz.
     * @return The audio data in a float array.
     */
    public static float[] byteArrToFloatArr(byte[] audioData) {
        float[] audioDataFloat = new float[audioData.length / 2];
        for (int i = 0; i < audioDataFloat.length; i++)
            audioDataFloat[i] = ((audioData[i * 2] & 0xff) | (audioData[i * 2 + 1] << 8)) / 32767.0f;

        return audioDataFloat;
    }
}
