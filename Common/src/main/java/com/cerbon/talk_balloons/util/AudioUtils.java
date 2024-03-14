package com.cerbon.talk_balloons.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public final class AudioUtils {

    /**
     * Audio format (48000 Hz, 16 bit, mono, little endian, pcm coding).
     */
    public static final AudioFormat FORMAT_PCM_48KHZ = new AudioFormat(
            48000, // Sample rate
            16, // Sample Size in Bits
            1, // Mono
            true, // Signed
            false // Byte order: little endian
    );

    /**
     * Audio format (16000 Hz, 16 bit, mono, little endian, pcm coding).
     */
    public static final AudioFormat FORMAT_PCM_16KHZ = new AudioFormat(
            16000, // Sample rate
            16, // Sample Size in Bits
            1, // Mono
            true, // Signed
            false // Byte order: little endian
    );

    /**
     * Converts raw audio data from voice chat into a format that can be read by Whisper.
     *
     * @param micAudio The raw audio data from the microphone. This data should be in PCM 48KHZ format.
     * @return An array of floating-point values representing the audio samples. These samples are in PCM 16KHZ format and are normalized to the range [-1.0, 1.0].
     */
    public static float[] convertToWhisperFormat(byte[] micAudio) {
        try {
            ByteArrayInputStream micAudioBytesInputStream = new ByteArrayInputStream(micAudio);

            AudioInputStream sourceInputStream = new AudioInputStream(micAudioBytesInputStream, FORMAT_PCM_48KHZ, micAudio.length);
            AudioInputStream targetInputStream = convert(sourceInputStream, FORMAT_PCM_16KHZ);

            byte[] targetAudioBytes = targetInputStream.readAllBytes();
            ShortBuffer shortBuffer = ByteBuffer.wrap(targetAudioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

            short[] targetAudioShorts = new short[shortBuffer.capacity()];
            shortBuffer.get(targetAudioShorts);

            float[] targetAudioFloats = new float[targetAudioShorts.length];
            for (int i = 0; i < targetAudioShorts.length; i++)
                targetAudioFloats[i] = ((float)targetAudioShorts[i])/0x8000;

            return targetAudioFloats;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AudioInputStream convert(AudioInputStream sourceStream, AudioFormat targetFormat) {
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }
}
