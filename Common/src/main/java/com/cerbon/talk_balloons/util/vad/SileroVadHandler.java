package com.cerbon.talk_balloons.util.vad;

import ai.onnxruntime.OrtException;

public class SileroVadHandler {
    private static final String MODEL_PATH = "C:\\Users\\felip\\Projects\\Minecraft Mods\\Talk-Balloons\\Common\\src\\main\\resources\\silero_vad.onnx";
    private static final int SAMPLE_RATE = 16000;
    private static final float START_THRESHOLD = 0.6f;
    private static final float END_THRESHOLD = 0.45f;
    private static final int MIN_SILENCE_DURATION_MS = 600;
    private static final int SPEECH_PAD_MS = 500;

    private static SileroVadDetector vadDetector;

    public static void loadVadDetector() {
        try {
            vadDetector = new SileroVadDetector(MODEL_PATH, START_THRESHOLD, END_THRESHOLD, SAMPLE_RATE, MIN_SILENCE_DURATION_MS, SPEECH_PAD_MS);

        } catch (OrtException e) {System.err.println("Error initializing the VAD detector: " + e.getMessage());}
    }

    public static SileroVadDetector getVadDetector() {
        return vadDetector;
    }
}
