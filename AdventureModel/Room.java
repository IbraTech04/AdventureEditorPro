package AdventureModel;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class contains the information about a 
 * room in the Adventure Game.
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @version 1.1
 */
public class Room {
    /**
     * The number of the room.
     */
    private int roomNumber;

    /**
     * The name of the room.
     */
    private String roomName;

    /**
     * The description of the room.
     */
    private String roomDescription;

    /**
     * The passage cycle for the room.
     */

    private Map<Connection, Room> passages;

    /**
     * The list of objects in the room.
     */
    public ArrayList<AdventureObject> objectsInRoom = new ArrayList<AdventureObject>();

    /**
     * A boolean to store if the room has been visited or not
     */
    private boolean isVisited;

    /**
     * AdvGameRoom constructor.
     *
     * @param roomName: The name of the room.
     * @param roomNumber: The number of the room.
     * @param roomDescription: The description of the room.
     */
    public Room(String roomName, int roomNumber, String roomDescription, String adventureName){
        this.roomName = roomName;
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
        this.isVisited = false;
        this.passages = new LinkedHashMap<>();
    }


    /**
     * Returns a comma delimited list of every
     * object's description that is in the given room,
     * e.g. "a can of tuna, a beagle, a lamp".
     *
     * @return delimited string of object descriptions
     */
    public String getObjectString() {
        return objectsInRoom.stream().map(AdventureObject::getDescription).collect(Collectors.joining(", "));
    }

    /**
     * Returns all directions it is possible to move in from this room.
     */
    public Collection<String> getAllDirections() {
        return this.passages.keySet().stream().map(Connection::direction).collect(Collectors.toSet());
    }

    /**
     * Returns a comma delimited list of every
     * move that is possible from the given room,
     * e.g. "DOWN, UP, NORTH, SOUTH".
     *
     * @return delimited string of possible moves
     */
    public String getCommands() {
        return String.join(", ", this.getAllDirections());
    }

    /**
     * This method adds a game object to the room.
     *
     * @param object to be added to the room.
     */
    public void addGameObject(AdventureObject object){
        this.objectsInRoom.add(object);
    }

    /**
     * This method removes a game object from the room.
     *
     * @param object to be removed from the room.
     */
    public void removeGameObject(AdventureObject object){
        this.objectsInRoom.remove(object);
    }

    /**
     * This method checks if an object is in the room.
     *
     * @param objectName Name of the object to be checked.
     * @return true if the object is present in the room, false otherwise.
     */
    public boolean checkIfObjectInRoom(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return true;
        }
        return false;
    }

    /**
     * Getter for returning an AdventureObject with a given name
     *
     * @param objectName: Object name to find in the room
     * @return: AdventureObject
     */
    public AdventureObject getObject(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return this.objectsInRoom.get(i);
        }
        return null;
    }

    /**
     * Getter method for the number attribute.
     *
     * @return number of the room
     */
    public int getRoomNumber(){
        return this.roomNumber;
    }

    /**
     * Getter method for the description attribute without new line char.
     *
     * @return description of the room
     */
    public String getUnsanitizedRoomDescription(){
        return this.roomDescription;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the room
     */
    public String getRoomName(){
        return this.roomName;
    }

    /**
     * Getter method for the end attribute.
     *
     * @return end status of the room
     */
    public boolean getEndStatus() {return this.passages.containsValue(null);}

    /**
     * Getter method for the start status of the room.
     *
     * @return start status of the room
     */
    public boolean getStartStatus() {return this.roomNumber == 1;}

    /**
     * Getter method for the passages attribute.
     *
     * @return passages of the room
     */
    public Map<Connection, Room> getPassages() {
        return this.passages;
    }

    /**
     * Getter method for the objectsInRoom attribute.
     *
     * @return list of objects in the room
     */
    public Collection<AdventureObject> getObjectsInRoom() {return this.objectsInRoom;}

    /**
     * Setter method for the roomName attribute.
     *
     * @param roomName: number of the room
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Setter method for the roomDescription attribute.
     *
     * @param roomDescription: description of the room
     */
    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }



    /**
     * Adds a gate to the room.
     *
     * @param direction: direction of the gate
     * @param object: lock for the gate
     * @param room: room to be connected to
     */
    public void addGate(String direction, String object, Room room) {
        this.passages.put(new Connection(direction, object), room);
    }

    /**
     * Clears all gates from the room.
     */
    public void deleteAllGates() {this.passages.clear();}

}
