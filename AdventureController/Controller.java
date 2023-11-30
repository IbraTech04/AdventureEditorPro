package AdventureController;

import AdventureModel.AdventureGame;
import AdventureModel.Connection;
import AdventureModel.Room;
import views.ViewAdventureEditor;
import views.VisualizerView;


import java.util.Collection;
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
        this.view.setController(this);
        // Do initial updates
        this.view.updateAllRooms(getAllRooms());
    }

    /**
     * getAllRooms
     *
     * @return List<Room></Rooms> a list of all the rooms in the game
     *
     */
    private Collection<Room> getAllRooms() {
        return model.getRooms().values();
    }

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

    public void updateRoomName(Room room, String name) {
        room.setRoomName(name);
        view.updateAllRooms(getAllRooms());
    }

    public void updateRoomDescription(Room room, String description) {
        room.setRoomDescription(description);
        view.updateAllRooms(getAllRooms());
    }

    public void deleteRoom(Room room) {
        // If the room ID is 1, raise an error (you can't delete the first room)
        if(room.getRoomNumber() == 1) {
            throw new IllegalArgumentException("You can't delete the starting room!");
        }
        model.deleteRoom(room);
        view.updateAllRooms(getAllRooms());
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
        view.updateAllRooms(getAllRooms());
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
        room1.getPassages().remove(pair);
        // Step 2: Update the view
        view.updateAllRooms(getAllRooms());
    }

    public void visualizeGatesFromRoom(Room room) {
        new VisualizerView(room);
    }
}
