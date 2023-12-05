package tts;

import java.util.concurrent.CompletableFuture;

public interface TTS {
    public CompletableFuture<Void> speak(String text);
}
