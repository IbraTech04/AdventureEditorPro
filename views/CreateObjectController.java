package views;
import AdventureController.Controller;
import AdventureModel.Room;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.*;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;

/**
 * CreateObjectController Class
 * Intermediary between the CreateObject view and the Main Controller
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @author Abigail Yanku
 * @version 1.0
 * */
public class CreateObjectController {

    /**
     *The main controller
     */
    Controller mainController;
    /**
     *The current room
     */
    Room currentRoom;
    /**
     *The chosen name of the object
     */
    String objectName;
    /**
     *The chosen description of the object
     */
    String objectDescription;
    /**
     *The chosen image of the object
     */
    String objectImage;

    @FXML
    private TextField objectNameField;
    @FXML
    private TextArea objectDescriptionField;
    @FXML
    private Button addImageButton;
    @FXML
    private ImageView objectImageView;
    @FXML
    private Button createButton;

    /**
     * addMainController
     * Adds a connection between the Main Controller and the CreateObjectController
     *
     * @param controller The main controller
     * */
    public void addMainController(Controller controller) {
        this.mainController = controller;
    }

    /**
     * setCurrentRoom
     * Sets the current room
     *
     * @param room The current room
     * */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * addListeners
     * Adds listeners to objectNameField and objectDescriptionField
     * */
    public void addListeners(){
        objectNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.objectName = newValue;
        });
        objectDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.objectDescription = newValue;
        });
    }

    /**
     * handleAddImageButton
     * Handles the add image button
     *
     * @param event The event
     * */
    public void handleAddImageButton(ActionEvent event) {
        Stage stage = (Stage) addImageButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            this.objectImage = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + objectImage);
            objectImageView.setImage(image);
        }
    }

    /**
     * handleCreateButton
     * Handles the create button
     *
     * @param event The event
     * */
    public void handleCreateButton(ActionEvent event) {
        mainController.addObjectToRoom(currentRoom, objectName, objectDescription, objectImage);
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
