package AdventureController;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.AdventureSaver;
import AdventureModel.Connection;
import AdventureModel.Room;
import views.Dialogs;
import views.FolderChooseDialog;
import views.ViewAdventureEditor;
import views.VisualizerView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import javafx.scene.control.Alert;



/**
 * Controller Class
 * Intermediary between the Model and the View
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @author Ibrahim Chehab
 * @author Themba Dube
 * @author Abigail Yanku
 * @version 1.1
 * */
public class Controller {
    private final AdventureGame model;
    private final ViewAdventureEditor view;
    // "What a Scam" - Themba 2023
    public Controller(AdventureGame model, ViewAdventureEditor view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        // Do initial updates
        this.view.updateAllRooms(getAllRooms());
    }

    /**
     * getAllRooms
     *
     * @return List<Room></Rooms> a list of all the rooms in the game
     */
    public Collection<Room> getAllRooms() {
        return model.getRooms().values();
    }

    /**
     * getAllObjects
     *
     * @return List<AdventureObject></AdventureObject> a list of all the objects in the game
     */
    public Collection<AdventureObject> getAllObjects() {
        Collection<AdventureObject> objects = new ArrayList<>();
        for (Room room : getAllRooms()) {
            objects.addAll(room.objectsInRoom);
        }
        return objects;
    }

    /**
     * addRoom
     * __________________________
     * Adds a room to the game
     */
    public void addRoom(){
        // Step 1: Find the max ID of the rooms, and add one to it to get the new room ID
        Collection<Room> rooms = getAllRooms();
        int newID = rooms.stream().mapToInt(Room::getRoomNumber).max().orElse(0) + 1;
        // Step 2: Actually make the room
        Room room = new Room("New Room (" + newID + ")", newID, "", "");
        model.addRoom(room);
        // Step 3: Update the view
        view.updateAllRooms(rooms);
    }

    /**
     * updateRoomName
     * __________________________
     * Updates the name of a room
     * @param room Room to update name of
     * @param name New name of room
     */
    public void updateRoomName(Room room, String name) {
        room.setRoomName(name);
        view.updateAllRooms(getAllRooms());
    }

    /**
     * updateRoomDescription
     * __________________________
     * Updates the description of a room
     * @param room Room to update description of
     * @param description New description of room
     */
    public void updateRoomDescription(Room room, String description) {
        room.setRoomDescription(description);
        view.updateAllRooms(getAllRooms());
    }

    /**
     * updateImage
     * __________________________
     * Updates the image of a room or object
     * @param room Room or Object to update image of
     * @param imagename Name of  image of room or object
     */
    public void updateImage(File selectedFile, Room room, String folder, String imagename) {
        String filePath = model.getDirectoryName();
        Path sourcePath = selectedFile.toPath();
        String desttofolder = filePath + File.separator + folder;
        Path destinationPath = Path.of(desttofolder + File.separator + imagename);
        try {
            try {
                Files.createDirectory(Path.of(desttofolder));
            } catch(IOException ignored) { // dir already exists
            }
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Error adding image", e.toString());
        }

        view.updateRoomView();
    }

    /**
     * updateEndStatus
     * __________________________
     * Updates the end status of a room
     * @param room Room to update status of
     * @param isEndRoom New end status of room
     */
    public void updateEndStatus(Room room, boolean isEndRoom) {
        if(isEndRoom) {
            // If the room ID is 1, raise an error (you can't make the first room an end room)
            if (room.getRoomNumber() == 1) {
                Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Room error. You cannot make the starting room an end room.");
                view.forceUncheckEnd();
                throw new IllegalArgumentException("You can't make the starting room an end room!");
            } else {
                var result = Dialogs.showDialogAndWait(Alert.AlertType.CONFIRMATION, "Are you sure you want to make this room an end room?", "All gates from this room will be removed.");
                if (result.getText().equals("OK")) { // If the user clicks OK
                    room.deleteAllGates(); // Delete all gates from the room
                    this.addGateToRoom(room, null, "FORCED"); //The only gate is a forced gate to null
                } else {
                    view.forceUncheckEnd();
                }
            }
        } else {
            room.deleteAllGates();// Delete all gates from the room
        }
        view.updateAllRooms(getAllRooms());
        view.updateAllGates(room.getPassages());
    }

    /**
     * deleteRoom
     * __________________________
     * Deletes a room from the game
     * @param room Room to delete
     */
    public void deleteRoom(Room room) {
        // If the room ID is 1, raise an error (you can't delete the first room)
        if(room.getRoomNumber() == 1) {
            Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "You cannot delete the starting room.");
            throw new IllegalArgumentException("You can't delete the starting room!");
        }
        var confirmResult = Dialogs.showDialogAndWait(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this room?", "This action cannot be undone.");

        if (confirmResult.getText().equals("OK")) { // If the user clicks OK
            // Delete any passage with the now-deleted room as a destination
            for (Room r : getAllRooms()) {
                r.getPassages().entrySet().removeIf(e -> Objects.equals(e.getValue(), room));
            }
            // Delete the room
            model.deleteRoom(room);
            if(view.getCurrentlySelectedRoom() == room) {
                // Move to the first room in the list
                view.setCurrentlySelectedRoom(getAllRooms().iterator().next());
            }
            view.updateAllGates(view.getCurrentlySelectedRoom().getPassages());
            view.updateAllRooms(getAllRooms());
        }
    }

    /**
     * addGateToRoom
     * __________________________
     * Adds a passage from room1 to room2 in the specified direction
     * @param room1 Room to add passage to
     * @param room2 Destination room
     * @param direction Direction of passage
     */
    public void addGateToRoom(Room room1, Room room2, String direction) {
        this.addGateToRoom(room1, room2, direction, null);
        view.updateAllGates(room1.getPassages());
    }

    /**
     * addGateToRoom
     * __________________________
     * Adds a passage from room1 to room2 in the specified direction
     * @param room1 Room to add passage to
     * @param room2 Destination room
     * @param direction Direction of passage
     * @param object Object to block passage (null if no object)
     * @author Ibrahim Chehab
     */
    public void addGateToRoom(Room room1, Room room2, String direction, String object) {
        // Step 1: Add the gate to the room
        room1.addGate(direction, object, room2);
        // Step 2: Update the view
        view.updateAllGates(room1.getPassages());
    }

    /**
     * deleteGateFromRoom
     * __________________________
     * Deletes a passage from room1 to room2 in the specified direction
     * @param room1 Room to delete passage from
     * @param pair Connection object containing the room and direction
     */
    public void deleteGateFromRoom(Room room1, Connection pair) {
        // Step 1: Delete the gate from the room
        var result = Dialogs.showDialogAndWait(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this gate?", "This action cannot be undone.");
        if (result.getText().equals("OK")) {
            room1.getPassages().remove(pair);
            // Step 2: Update the view
            view.updateAllGates(room1.getPassages());
        }
    }

    /**
     * addObjectToRoom
     * __________________________
     * Adds an object to a room
     * @param room Room to add object to
     * @param objectName Name of object
     * @param objectDescription Description of object
     * @param imageFile File of object image
     */
    public void addObjectToRoom(Room room, String objectName, String objectDescription, File imageFile, String dest) {
        //TODO: Add code here to add the object image to the image folder
        AdventureObject newObject = new AdventureObject(objectName, objectDescription, room);
        room.addGameObject(newObject);
        updateImage(imageFile, room, dest, objectName);
        view.updateAllObjects(room.getObjectsInRoom());
    }

    /**
     * deleteObjectFromRoom
     * __________________________
     * Deletes an object from a room
     * @param room Room to delete object from
     * @param object Object to delete
     */
    public void deleteObjectFromRoom(Room room, AdventureObject object){
        //Add code here to remove object image from the image folder
        var result = Dialogs.showDialogAndWait(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this object?", "This action cannot be undone.");
        if (result.getText().equals("OK")){
            room.removeGameObject(object);
            view.updateAllObjects(room.getObjectsInRoom());
        }
    }

    /**
     * visualizeGatesFromRoom
     * __________________________
     * Visualizes the gates from a room
     */
    public void visualizeGatesFromRoom(Room room) {
        new VisualizerView(room);
    }

    public void onLoadRequest() {
        // Display system folder selection dialog
        File choice = FolderChooseDialog.getSelectedFolder(view.getStage());
        if(choice != null) {
            AdventureBootstrap.loadAndDisplayGame(choice.getName());
        }
    }

    public void onSaveRequest() {
        // Display system folder selection dialog
        File choice = FolderChooseDialog.getSelectedFolder(view.getStage());
        if(choice != null) {
            AdventureSaver saver = new AdventureSaver(this.model, choice.getName());
            try {
                saver.saveGame();
            } catch(IOException e) {
                Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Error saving file: " + e);
            }
        }
    }
}
