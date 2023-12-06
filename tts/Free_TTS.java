package tts;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javax.sound.sampled.AudioFileFormat.Type;
import java.io.File;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Free_TTS implements TTS {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
        Thread t = new Thread(runnable, "Speech thread");
        t.setDaemon(true);
        return t;
    });

    /**
     * VOICENAME_kevin
     * __________________________
     * The name of the voice to use
     * Speaks action whenever the user presses a button
     */
    private static final String VOICENAME_kevin = "kevin16";
    private String voiceName;
    private Voice voice;
    String sep = File.separator;
    AudioPlayer audioPlayer;

    static {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    public Free_TTS() {
        this.voiceName = VOICENAME_kevin;
        this.voice = VoiceManager.getInstance().getVoice(this.voiceName);
        this.voice.allocate();
    }

    public CompletableFuture<Void> speak(String text) {
        return CompletableFuture.runAsync(() -> {
            this.voice.speak(text);
        }, EXECUTOR);
    }


}
