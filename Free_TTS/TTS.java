package Free_TTS;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS implements freeTTS {
    /**
     * VOICENAME_kevin
     * __________________________
     * The name of the voice to use
     * Speaks action whenever the user presses a button
     */
    private static final String VOICENAME_kevin = "kevin16";
    private String voiceName;
    private Voice voice;

    static {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    public TTS() {
        this.voiceName = VOICENAME_kevin;
        this.voice = VoiceManager.getInstance().getVoice(this.voiceName);
        this.voice.allocate();
    }

    public void speak(String text) {
        this.voice.speak(text);
    }


}
