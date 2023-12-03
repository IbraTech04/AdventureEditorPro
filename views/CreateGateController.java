package views;
import AdventureController.Controller;
import AdventureModel.AdventureObject;
import AdventureModel.Room;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;


/**
 * CreateGateController Class
 * Intermediary between the CreateGate view and the Main Controller
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @author Abigail Yanku
 * @version 1.0
 */
public class CreateGateController {

    /**
     *The main controller
     */
    Controller mainController;
    /**
     *The chosen object
     */
    AdventureObject chosenObject;
    /**
     *The current room
     */
    Room currentRoom;
    /**
     *The chosen room
     */
    Room chosenRoom;
    /**
     *The rooms in the game.
     */
    Collection<Room> rooms;
    /**
     *The objects in the game.
     */
    Collection<AdventureObject> objects;

    @FXML
    private ChoiceBox<Room> listRooms;
    @FXML
    private ChoiceBox<AdventureObject> listObjects;
    @FXML
    private TextField gateDirection;
    @FXML
    private CheckBox isForced;
    @FXML
    private Button createButton;

    /**
     * addMainController
     * @param controller the main controller
     */
    public void addMainController(Controller controller) {
        this.mainController = controller;
    }

    /**
     * setRoomList
     * @param rooms the rooms in the game
     */
    public void setRoomList(Collection<Room> rooms) {
        this.rooms = rooms;
        listRooms.getItems().addAll(rooms);
        listRooms.setConverter(new StringConverter<>() {
            @Override
            public String toString(Room room) {
                return room.getRoomName();
            }
            @Override
            public Room fromString(String string) {
                return null;
            }
        });
        listRooms.getSelectionModel().selectFirst();
        this.chosenRoom = listRooms.getValue();
        listRooms.setOnAction(this::updateGateRoom);
    }

    /**
     * updateGateRoom
     * @param event the event
     */
    public void updateGateRoom(ActionEvent event) {
        this.chosenRoom = listRooms.getValue();
    }
    /**
     * setObjectList
     * @param objects the objects in the game
     */
    public void setObjectList(Collection<AdventureObject> objects) {
        this.objects = objects;
        listObjects.getItems().add(new AdventureObject("None", null, null));
        listObjects.getItems().addAll(objects);
        listObjects.setConverter(new StringConverter<>() {
            @Override
            public String toString(AdventureObject object) {
                return object.getName();
            }
            @Override
            public AdventureObject fromString(String string) {
                return null;
            }
        });
        listObjects.getSelectionModel().selectFirst();
        listObjects.setOnAction(this::updateGateObject);
    }

    /**
     * updateGateObject
     * @param event the event
     */
    public void updateGateObject(ActionEvent event) {
        this.chosenObject = listObjects.getValue();
    }

    /**
     * setCurrentRoom
     * @param room the current room
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * handleIsForced
     * @param event the event
     */
    public void handleIsForced(ActionEvent event) {
        if (isForced.isSelected()) {
            gateDirection.setText("FORCED");
        } else {
            gateDirection.setText("");
        }
        gateDirection.setDisable(isForced.isSelected());

    }

    /**
     * handleCreateButton
     * @param event the event
     */
    public void handleCreateButton(ActionEvent event) {
        if (gateDirection.getText().isEmpty()||(gateDirection.getText().equals("FORCED") && !isForced.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Gate");
            alert.setContentText("Please enter a valid direction.");
            alert.showAndWait();
            throw new IllegalArgumentException("No direction entered");
        } else if (currentRoom.getEndStatus()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Gate");
            alert.setContentText("You cannot add a gate to an end room.");
            alert.showAndWait();
            throw new IllegalArgumentException("You can't add a gate to an end room");
        } else{
            if (this.chosenObject != null) {
                mainController.addGateToRoom(currentRoom, chosenRoom, gateDirection.getText(), chosenObject.getName());
            } else {
                mainController.addGateToRoom(currentRoom, chosenRoom, gateDirection.getText());
            }
        }

        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
