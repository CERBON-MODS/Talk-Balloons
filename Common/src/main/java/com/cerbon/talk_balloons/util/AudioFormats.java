package com.cerbon.talk_balloons.util;

import javax.sound.sampled.AudioFormat;

public final class AudioFormats {

    /**
     * Audio format (48000 Hz, 16 bit, mono, little endian, pcm coding).
     */
    public static final AudioFormat PCM_48KHZ = new AudioFormat(
            48000, // Sample rate
            16, // Sample Size in Bits
            1, // Mono
            true, // Signed
            false // Byte order: little endian
    );

    /**
     * Audio format (16000 Hz, 16 bit, mono, little endian, pcm coding).
     */
    public static final AudioFormat PCM_16KHZ = new AudioFormat(
            16000, // Sample rate
            16, // Sample Size in Bits
            1, // Mono
            true, // Signed
            false // Byte order: little endian
    );
}
