package views;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.*;



/**
 * Class CreateObject.
 *
 * Displays the create object menu.
 */

public class CreateObject {

    public CreateObject() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateObject.fxml"));
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
    }
}
