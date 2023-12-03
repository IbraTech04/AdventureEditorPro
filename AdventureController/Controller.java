package AdventureController;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Connection;
import AdventureModel.Room;
import views.ViewAdventureEditor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
                view.forceUncheckEnd();
                throw new IllegalArgumentException("You can't make the starting room an end room!");
            } else{
                // Change this for a different implementation
                room.deleteAllGates(); // Delete all gates from the room
                this.addGateToRoom(room, null, "FORCED"); //The only gate is a forced gate to null
            }
        } else {
            this.deleteGateFromRoom(room, new Connection("FORCED", null)); // Delete the forced gate
        }
        view.updateAllRooms(getAllRooms());
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
            throw new IllegalArgumentException("You can't delete the starting room!");
        }
        model.deleteRoom(room);
        for (Room r : getAllRooms()) {
            while (r.getPassages().containsValue(room)) {
                String direction = r.getPassages().entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), room)).findFirst().get().getKey().direction();
                String object = r.getPassages().entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), room)).findFirst().get().getKey().lock();
                r.deleteGate(direction, object);
            }
        }
        view.updateAllGates(room.getPassages());
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
        room1.getPassages().remove(pair);
        // Step 2: Update the view
        view.updateAllGates(room1.getPassages());
    }

    /**
     * addObjectToRoom
     * __________________________
     * Adds an object to a room
     * @param room Room to add object to
     * @param objectName Name of object
     * @param objectDescription Description of object
     * @param objectImage Image path of object image
     */
    public void addObjectToRoom(Room room, String objectName, String objectDescription, String objectImage){
        //TODO: Do this properly
        AdventureObject newObject = new AdventureObject(objectName, objectDescription, room);
        //Add code here to add the object image to the image folder
        room.addGameObject(newObject);
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
        room.removeGameObject(object);
        view.updateAllObjects(room.getObjectsInRoom());
    }

}
