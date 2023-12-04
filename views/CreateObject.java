package views;
import AdventureController.Controller;
import AdventureModel.Room;
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
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @author Abigail Yanku
 * @version 1.0
 */

public class CreateObject {

    /**
     * CreateObject
     * Loads the create object view
     *
     * @param controller The main controller
     * @param room The current room
     * */
    public CreateObject(Controller controller, Room room) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateObject.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // Send information to create object controller
        CreateObjectController createObjectController = loader.getController();
        createObjectController.addMainController(controller);
        createObjectController.setCurrentRoom(room);
        stage.setScene(new Scene(root));
        // Set the icon
        stage.getIcons().add(new Image("assets/icon.png"));
        // Set the title
        stage.setTitle("AdventureEditorPro");
        // Disable Resizing
        stage.setResizable(false);
        stage.show();
        createObjectController.addListeners();
    }
}
