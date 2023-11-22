package views;

public class PersistentWarning implements WarningInterface{
    ViewAdventureEditor adventureEditorView;

    public PersistentWarning(ViewAdventureEditor adventureEdiView){
        this.adventureEditorView = adventureEdiView;

    }


        @Override
        public void displayWarning(String message) {
            System.out.println(message);
        }
}
