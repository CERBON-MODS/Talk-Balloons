package com.cerbon.talk_balloons.util.stt;

import io.github.givimad.whisperjni.WhisperContext;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class WhisperHandler {
    private static WhisperJNI whisper;
    private static WhisperFullParams whisperParams;
    private static WhisperContext context;

    private static final Path modelPath = Path.of("C:/Users/felip/Projects/Minecraft Mods/Talk-Balloons/Common/src/main/resources/ggml-model-whisper-base.bin");

    private static final List<String> transcribedText = new ObjectArrayList<>();

    /**
        Should be called at first.
     */
    public static void loadWhisper() {
        try {
            WhisperJNI.loadLibrary();

            whisper = new WhisperJNI();
            whisperParams = new WhisperFullParams();

            context = whisper.init(modelPath);

            whisperParams.language = "auto"; // Detect spoken language automatically

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param audioData The audio data in a float array. (Must be in 16khz).
     */
    public static void transcribeAudioToText(float[] audioData) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        threadPool.execute(() -> {
            int result = whisper.full(context, whisperParams, audioData, audioData.length);
            if (result != 0)
                throw new RuntimeException("Transcription failed with code " + result);

            int numSegments = whisper.fullNSegments(context);
            for (int i = 0; i < numSegments; i++) {
                String text = whisper.fullGetSegmentText(context, i);
                transcribedText.add(text);
            }
        });

        if (!threadPool.isShutdown())
            threadPool.shutdown();
    }

    /**
     * Retrieves and clears the most recent transcribed text.
     *
     * This method creates a copy of the current transcribed text, then clears the original list for subsequent inputs.
     *
     * @return A list of strings representing the most recent transcribed text. If no transcription has occurred it returns an empty list.
     */
    public static List<String> getTranscribedText() {
        List<String> transcribedTextCopy = new ObjectArrayList<>(transcribedText);
        transcribedText.clear();
        return transcribedTextCopy;
    }
}
