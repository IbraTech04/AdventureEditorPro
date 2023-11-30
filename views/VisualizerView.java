package views;


import AdventureModel.Connection;
import AdventureModel.Room;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import gptfx.GraphCanvas;

import java.util.*;
import java.util.stream.Collectors;

public class VisualizerView {
    private static final float NODE_RADIUS = 20;
    private static final float RESTRICTED_ANGLE = 45;
    private static final float DISTANCE = 200;
    private GraphCanvas canvas;
    private Stage stage;

    private record RoomAndPosition(Point2D position, Room room, float incomingAngle) {}

    public VisualizerView(Room startNode) {
        this.stage = new Stage();
        stage.setTitle("Visualizer");
        stage.show();

        canvas = new GraphCanvas(stage.getWidth(), stage.getHeight());
        stage.setScene(new Scene(new VBox(canvas))); // "Why do I have to give it a VBox with one child?" - Themba

        // BFS iteration over all rooms with the start node being the given room
        Queue<RoomAndPosition> roomsToIterate = new ArrayDeque<>();
        Map<Room, GraphCanvas.CircularNode> seenRooms = new HashMap<>();
        roomsToIterate.add(new RoomAndPosition(new Point2D((canvas.getWidth() - NODE_RADIUS) / 2, (canvas.getHeight() - NODE_RADIUS) / 2), startNode, Float.NaN));
        while(!roomsToIterate.isEmpty()) {
            RoomAndPosition entry = roomsToIterate.remove();
            if (!seenRooms.containsKey(entry.room())) {
                // room has not been seen before
                GraphCanvas.CircularNode theNode = canvas.addNode(entry.position.getX(), entry.position.getY(), NODE_RADIUS);
                seenRooms.put(entry.room, theNode);
                float minAngle, maxAngle;
                if(Float.isNaN(entry.incomingAngle)) {
                    minAngle = 0;
                    maxAngle = 360;
                } else {
                    minAngle = entry.incomingAngle - RESTRICTED_ANGLE;
                    maxAngle = entry.incomingAngle + RESTRICTED_ANGLE;
                }
                int numOutgoing = (int)entry.room.getPassages().values().stream().distinct().count();
                float angleBetween = (maxAngle - minAngle) / numOutgoing;
                float curAngle = minAngle;
                Map<Room, List<Map.Entry<Connection, Room>>> connectionsByRoom = entry.room.getPassages().entrySet().stream()
                        .filter(e -> e.getValue() != null)
                        .collect(Collectors.groupingBy(Map.Entry::getValue));
                for(Map.Entry<Room, List<Map.Entry<Connection, Room>>> connectionEntry : connectionsByRoom.entrySet()) {
                    double newX = entry.position.getX() + (DISTANCE * Math.cos(curAngle * Math.PI / 180));
                    double newY = entry.position.getY() + (DISTANCE * Math.sin(curAngle * Math.PI / 180));
                    roomsToIterate.add(new RoomAndPosition(new Point2D(newX, newY), connectionEntry.getKey(), curAngle));
                    curAngle += angleBetween;
                }
            }
        }
        // TODO draw connections between rooms, labels, etc
    }
}
