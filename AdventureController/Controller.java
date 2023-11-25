package AdventureController;

import AdventureModel.AdventureGame;
import AdventureModel.Room;
import views.ViewAdventureEditor;


import java.util.List;

/**
 * Controller Class
 * Intermediary between the Model and the View
 *
 * @author Ibrahim Chehab
 * @author Themba Dube
 * @version 1.0
 * */
public class Controller {
    private final AdventureGame model;
    private final ViewAdventureEditor view;
    // "What a Scam" - Themba 2023
    public Controller(AdventureGame model, ViewAdventureEditor view) {
        this.model = model;
        this.view = view;
    }

    /**
     * getAllRooms
     *
     * @return List<Room></Rooms> a list of all the rooms in the game
     *
     */
    private List<Room> getAllRooms() {
        return model.getRooms().values().stream().toList();
    }

    public void addRoom(){
        // Step 1: Find the max ID of the rooms, and add one to it to get the new room ID
        List<Room> rooms = getAllRooms();
        int maxID = rooms.stream().mapToInt(Room::getRoomNumber).max().orElse(0);
        Room room = new Room("New Room (" + maxID + ")", maxID, "", "");
        model.addRoom(room);
        //TODO: Update View
    }

    /**
     * addGateToRoom
     * __________________________
     * Adds a passage from room1 to room2 in the specified direction
     * @param room1
     * @param room2
     * @param direction
     * @author Ibrahim Chehab
     */
    public void addGateToRoom(Room room1, Room room2, String direction) {

    }
}
