package gptfx;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX Abstraction Layer which renders a PassageCycle
 * @author Ibrahim Chehab, Themba Dube, ChatGPT
 * (ChatGPT was consulted for the design and implementation of this class, surprisingly it did not disappoint)
 */
public class GraphCanvas extends Canvas {
    private List<CircularNode> nodes;
    private List<Connection> connections;

    private double renderOffsetX, renderOffsetY;
    private Point2D currentDragPoint;

    private Scale zoomTransform = new Scale(1, 1);


    public GraphCanvas(double width, double height) {
        super(width, height);
        this.setOnMousePressed(this::onDragStart);
        this.setOnMouseDragged(this::onDrag);
        this.getTransforms().add(zoomTransform);
        this.setOnScroll(this::onZoom);
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

    /**
     * Zoom in and out of the canvas
     * @param event ScrollEvent to zoom on
     * @author Ibrahim Chehab
     */
    private void onZoom(ScrollEvent event) {
        double zoomFactor = 1.25;
        double deltaY = event.getDeltaY();

        if (deltaY < 0) {
            zoomFactor = 2.0 - zoomFactor;
        }

        Point2D pivotOnZoom = new Point2D(event.getX(), event.getY());
        // Zoom relative to the mouse position
        zoomTransform.setPivotX(pivotOnZoom.getX());
        zoomTransform.setPivotY(pivotOnZoom.getY());

        // Make sure the zoom factor isn't less than 1
        if (zoomTransform.getX() * zoomFactor < 1 || zoomTransform.getY() * zoomFactor < 1) {
            return;
        }

        zoomTransform.setX(zoomTransform.getX() * zoomFactor);
        zoomTransform.setY(zoomTransform.getY() * zoomFactor);

        redrawCanvas();
        event.consume();
    }

    /**
     * Called when the user starts dragging the canvas, keeps track of the relative drag point
     * @param event MouseEvent to track the drag point of
     * @author Themba Dube
     */
    private void onDragStart(MouseEvent event) {
        currentDragPoint = new Point2D(event.getScreenX(), event.getScreenY());
    }

    /**
     * Called when the user drags the canvas, moves the canvas relative to the drag point
     * @param event MouseEvent to track the drag point of
     * @author Themba Dube
     */
    private void onDrag(MouseEvent event) {
        renderOffsetX += event.getScreenX() - currentDragPoint.getX();
        renderOffsetY += event.getScreenY() - currentDragPoint.getY();
        currentDragPoint = new Point2D(event.getScreenX(), event.getScreenY());
        redrawCanvas();
    }

    /**
     * Adds a node to the canvas
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @param radius radius of the node
     * @param name name of the node, typically the room id
     * @return the node that was added to the canvas, for chaining purposes (e.g. canvas.addNode(...).connectNodes(...))
     */
    public CircularNode addNode(double x, double y, double radius, String name) {
        CircularNode node = new CircularNode(this, x, y, radius, name);
        nodes.add(node);
        redrawCanvas();
        return node;
    }

    /**
     * Connects two nodes within the canvas
     * @param src source node
     * @param dst destination node
     * @param locked The key required to unlock the room, null if no key is required
     * @param direction The direction of the connection (NORTH, SOUTH, EAST, WEST, FORCED, etc.)
     * @author Ibrahim Chehab, Themba Dube
     */
    public void connectNodes(CircularNode src, CircularNode dst, String locked, String direction) {
        // If the room is locked and the direction is forced, color red
        // If the room is forced, color purple
        // If the room is locked, color yellow
        // If the room is unlocked, color green
        Color color = Color.GREEN;
        if(locked != null && direction.equalsIgnoreCase("forced")) {
            color = Color.RED;
        } else if(locked != null) {
            color = Color.YELLOW;
        } else if(direction.equalsIgnoreCase("forced")) {
            color = Color.PURPLE;
        }
        connections.add(new Connection(this, src, dst, color));
        redrawCanvas();
    }

    /**
     * Clears the background of the canvas
     * @author Themba Dube
     */
    private void clearBackground() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Clears the canvas of all nodes and connections
     * @author Themba Dube
     */
    public void clear() {
        clearBackground();

        nodes.clear();
        connections.clear();
    }

    /**
     * Redraws the canvas with all nodes and connections
     * Typically used whenever a node or connection is added or removed from the canvas, or when the canvas is resized
     * @author Ibrahim Chehab, Themba Dube
     */
    private void redrawCanvas() {
        clearBackground();

        GraphicsContext gc = getGraphicsContext2D();

        for (Connection connection : connections) {
            connection.draw(gc);
        }

        for (CircularNode node : nodes) {
            node.draw(gc);
        }
    }

    /**
     * Represents a node within the canvas
     * @param canvas The canvas this node is on
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @param radius radius of the node
     * @param label label of the node, typically the room id
     * @author Themba Dube
     */
    public record CircularNode(GraphCanvas canvas, double x, double y, double radius, String label) {
        void draw(GraphicsContext gc) {
            gc.setFill(Color.BLUE);
            gc.fillOval(canvas.renderOffsetX + x - radius, canvas.renderOffsetY + y - radius, 2 * radius, 2 * radius);
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(24));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(label, canvas.renderOffsetX + x, canvas.renderOffsetY + y);
        }
    }

    private record Connection(GraphCanvas canvas, CircularNode node1, CircularNode node2, Color color) {

        /**
         * Draw a connection between two nodes
         * @param gc GraphicsContext to draw on
         * @author Themba Dube
         */
        public void draw(GraphicsContext gc) {

            gc.setStroke(this.color);
            gc.setLineWidth(2);

            double angle = Math.atan2(node2.y - node1.y, node2.x - node1.x);
            double arrowSize = 10;

            // Offset to put the points et so that it isn't behind the node
            double x1Reduction = (node1.radius * Math.cos(angle));
            double y1Reduction = (node1.radius * Math.sin(angle));
            double x2Reduction = (node2.radius * Math.cos(angle));
            double y2Reduction = (node2.radius * Math.sin(angle));

            // Draw line
            gc.strokeLine(canvas.renderOffsetX + node1.x + x1Reduction, canvas.renderOffsetY + node1.y + y1Reduction,
                    canvas.renderOffsetX + node2.x - x2Reduction, canvas.renderOffsetY + node2.y - y2Reduction);



            // Draw arrowhead at the end of the line
            drawArrowhead(gc, canvas.renderOffsetX + node2.x - x2Reduction, canvas.renderOffsetY + node2.y - y2Reduction, angle, arrowSize);
        }

        /**
         * Draw an arrowhead at the end of a line. Does lots of trigonometry to calculate the points of the arrowhead
         * @param gc GraphicsContext to draw on
         * @param x x coordinate of the arrowhead
         * @param y y coordinate of the arrowhead
         * @param angle angle of the arrowhead
         * @param arrowSize size of the arrowhead
         * @author Themba Dube
         */
        private void drawArrowhead(GraphicsContext gc, double x, double y, double angle, double arrowSize) {
            double arrowheadAngle = Math.toRadians(30);
            // Fancy math which calculates the translated points of the arrowhead
            double x1 = x - arrowSize * Math.cos(angle - arrowheadAngle);
            double y1 = y - arrowSize * Math.sin(angle - arrowheadAngle);
            double x2 = x - arrowSize * Math.cos(angle + arrowheadAngle);
            double y2 = y - arrowSize * Math.sin(angle + arrowheadAngle);

            gc.setFill(this.color);
            gc.fillPolygon(new double[]{x, x1, x2}, new double[]{y, y1, y2}, 3);
        }
    }
}
