package views;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import java.io.*;



/**
 * Class CreateGate. Display that allows users to create Gates/Passages to Rooms.
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 */

public class CreateGate {

    public CreateGate() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateGate.fxml"));
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
