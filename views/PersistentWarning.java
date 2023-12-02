package views;
import TTS.Free_TTS;

public class PersistentWarning implements WarningInterface{
    ViewAdventureEditor adventureEditorView;
    Free_TTS tts = new Free_TTS();

    public PersistentWarning(ViewAdventureEditor adventureEdiView){
        this.adventureEditorView = adventureEdiView;

    }


        @Override
        public void displayWarning(String message) {
            tts.speak(message);
            System.out.println(message);
        }
}
