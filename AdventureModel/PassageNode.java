package AdventureModel;

import java.util.HashMap;
import java.util.List;

/**
* Node in a Cycle representing a single room in the Cycle of possible paths
* @author Ibrahim Chehab (chehabib)
 */

public class PassageNode {

    private HashMap<String, PassageNode> rooms; //list of all rooms which can be accessed from this node,
    // along with their direction
    // Relates the direction to the room object
    private final int roomNumber; //room number of this node
    private final String roomName; //room name of this node

    private final Room room; //room object which this node represents

    private String objectRequiredToEnter = null; //object required to enter this room

    public PassageNode(int roomNumber, String roomName, Room room){
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.room = room;
    }

    public PassageNode(int roomNumber, String roomName, Room room, HashMap<String, PassageNode> paths){
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.room = room;
        this.rooms = paths;
    }


    /**
     * lockRoom
     *
     * Sets the object required to enter this room
     * @author Ibrahim Chehab
     */
    public void lockRoom(String object) {
        this.objectRequiredToEnter = object;
    }

    /**
     * unlockRoom
     *
     * Removes the object required to enter this room
     */
    public void unlockRoom() {
        this.objectRequiredToEnter = null;
    }

    /**
     * getLock
     *
     * Returns name of object required to enter this room
     * If no object is set, returns null
     */
    public String getLock() {
        return this.objectRequiredToEnter;
    }

    /**
     * getDirections
     * Returns all the possible directions which can be taken from this node
     * @author Ibrahim Chehab (chehabib)
     * @return List<String> list of all directions which can be taken from this node
     */
    public List<String> getDirections(){
        return List.copyOf(rooms.keySet());
    }

    /**
     * getNodes
     * Returns a list of all the nodes which this node has a path to
     * @author Ibrahim Chehab (chehabib
     * @return List<PassageNode> list of all nodes which this node has a path to
     */
    public List<PassageNode> getNodes(){
        return List.copyOf(rooms.values());
    }

    /**
     * getPassageTable
     * Returns a hashmap relating the directions to their respective Nodes
     * @author Ibrahim Chehab (chehabib)
     */
    public HashMap<String, PassageNode> getPassageTable() {
        return this.rooms;
    }

    /**
     * addRoom
     * Adds a room to the list of rooms which can be accessed from this node
     * @author Ibrahim Chehab (chehabib)
     */
    public void addRoom(String direction, Room room){
        this.addRoom(direction, room, null);
    }
    public void addRoom(String direction, Room room, String object){
        PassageNode node = new PassageNode(room.getRoomNumber(), room.getRoomName(), room);
        node.lockRoom(object);
        this.rooms.put(direction, node);
    }
}
