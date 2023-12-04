package views;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class FolderChooseDialog {
    private static final Path GAMES_FOLDER = new File("Games").toPath().toAbsolutePath();
    public static File getSelectedFolder(Stage stage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(GAMES_FOLDER.toFile());
        File choice = dc.showDialog(stage);
        if(choice == null) {
            return null;
        }
        if(!choice.isDirectory() || !choice.toPath().toAbsolutePath().startsWith(GAMES_FOLDER)) {
            ErrorDialog.showAndWait("Directory could not be opened or is not inside Games folder");
            return null;
        }
        return choice;
    }
}
