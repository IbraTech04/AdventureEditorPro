package views;

public class OptionalWarning implements WarningInterface{
    ViewAdventureEditor adventureEditorView;

    public OptionalWarning(ViewAdventureEditor adventureEdiView){
        this.adventureEditorView = adventureEdiView;

    }


        @Override
        public void displayWarning(String message) {
            System.out.println(message);
        }
}
