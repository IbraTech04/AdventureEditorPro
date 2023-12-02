package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import Free_TTS.*;

import java.io.File;

/**
 * Class WelcomeController.  Handles all the necessary tasks to run the Welcome screen.
 */
public class WelcomeController {

    /**
     * createProj
     * __________________________
     * Handles the createProj button press
     * Opens a folder selection window to select the directory to create the project in
     *
     * @param actionEvent the event that triggered this method
     * @author Ibrahim Chehab
     */
    TTS tts = new TTS();



    @FXML
    public void createProj(ActionEvent actionEvent) {
        tts.speak("Create Project");
        System.out.print("New Game button pressed");
    }

    /**
     * loadProj
     * __________________________
     * Handles the loadProj button press
     * Opens a folder selection window to select the directory to load the project from, then launches the main UI window
     *
     * @param actionEvent the event that triggered this method
     * @author Ibrahim Chehab
     */
    @FXML
    public void loadProj(ActionEvent actionEvent) {
        // Open a folder selection dialog
        tts.speak("Load Project");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Existing Project");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            System.out.println(selectedDirectory.getAbsolutePath());
            return;
        }

        Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "No directory selected, please try again.");
    }

    /**
     * quit
     * __________________________
     * Handles the quit button press
     * Closes the window
     *
     * @param actionEvent the event that triggered this method
     * @author Ibrahim Chehab
     */
    @FXML
    public void quit(ActionEvent actionEvent) {
        // Close the window
        tts.speak("Quit");
        System.exit(0);
    }
}
