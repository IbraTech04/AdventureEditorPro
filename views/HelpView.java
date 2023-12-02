package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Free_TTS.*;
import java.io.IOException;

/**
 * Class HelpView.
 *
 * Displays the help menu.
 */
public class HelpView {

    public HelpView() {
        TTS tts = new TTS();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stage.setScene(new Scene(root));
        // Set the icon
        stage.getIcons().add(new Image("assets/icon.png"));
        // Set the title
        stage.setTitle("AdventureEditorPro");
        // Disable Resizing
        stage.setResizable(false);
        stage.show();
        tts.speak("Instructions:\n" +
                "\n" +
                "This is an Adventure Game Creator.\n" +
                "To create an Adventure Game you can start by creating rooms.\n" +
                "You can add a room by clicking on the Create Room button. You can add gates to your room by pressing the add Gate button.\n" +
                "You must create a start and end room. Some of your rooms can force teleport a player to another room. Some rooms are also blocked by Objects.\n" +
                "To create an object, press the create object button. You can add pictures to objects and rooms. \n" +
                "After you are done, press  finish and you will be able to play the game you create. Enjoy!");
    }
}
