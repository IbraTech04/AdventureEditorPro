package TTS;
import com.sun.speech.freetts.FreeTTS;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javax.sound.sampled.AudioFileFormat.Type;
import java.io.File;

public class Free_TTS implements TTS {
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

    }

    public void speak(String text) {
        this.voice.allocate();
        this.voice.speak(text);
        this.voice.deallocate();
    }

    public void createAudioFile(String text, String filename) {
        this.voice.allocate();
        //create an audio player to dump the output file
        audioPlayer = new SingleFileAudioPlayer("Games"+sep+"TinyGame"+sep+"sounds"+sep+filename,Type.WAVE);
        //attach the audio player
        this.voice.setAudioPlayer(audioPlayer);
        this.voice.speak(text);
        this.voice.deallocate();
        audioPlayer.close();
    }
    


}
