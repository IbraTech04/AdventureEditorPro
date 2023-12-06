package AdventureController;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import views.Dialogs;
import views.ImageHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

    File imageFile;

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG files", "*.jpg"));
        imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            objectImage = imageFile.getAbsolutePath();
            if (objectImage.endsWith(".jpg")) {
                Image roomImageFile;
                try {
                    roomImageFile = ImageHelper.load(imageFile.toPath());
                } catch (IOException e) {
                    Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Error adding image", e.toString());
                    roomImageFile = null;
                }
                objectImageView.setImage(roomImageFile);
                objectImageView.setPreserveRatio(true);
            } else {
                Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Invalid file type. Please select a .jpg file.");
                imageFile = null;
                throw new IllegalArgumentException("Invalid File Type");
            }
        }
    }

    /**
     * handleCreateButton
     * Handles the create button
     *
     * @param event The event
     * */
    public void handleCreateButton(ActionEvent event) {
        mainController.addObjectToRoom(currentRoom, objectName, objectDescription, imageFile, "objectImages");
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
