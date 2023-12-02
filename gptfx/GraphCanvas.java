package gptfx;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX Abstraction Layer which renders a PassageCycle
 * @author Ibrahim Chehab, Themba Dube, ChatGPT
 */
public class GraphCanvas extends Canvas {
    private List<CircularNode> nodes;
    private List<Connection> connections;

    private double renderOffsetX, renderOffsetY;
    private Point2D currentDragPoint;

    public GraphCanvas(double width, double height) {
        super(width, height);
        this.setOnMousePressed(this::onDragStart);
        this.setOnMouseDragged(this::onDrag);
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

    private void onDragStart(MouseEvent event) {
        currentDragPoint = new Point2D(event.getScreenX(), event.getScreenY());
    }

    private void onDrag(MouseEvent event) {
        renderOffsetX += event.getScreenX() - currentDragPoint.getX();
        renderOffsetY += event.getScreenY() - currentDragPoint.getY();
        currentDragPoint = new Point2D(event.getScreenX(), event.getScreenY());
        redrawCanvas();
    }

    public CircularNode addNode(double x, double y, double radius, String name) {
        CircularNode node = new CircularNode(this, x, y, radius, name);
        nodes.add(node);
        redrawCanvas();
        return node;
    }

    public void connectNodes(CircularNode src, CircularNode dst) {
        connections.add(new Connection(this, src, dst));
        redrawCanvas();
    }

    private void clearBackground() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    public void clear() {
        clearBackground();

        nodes.clear();
        connections.clear();
    }

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

    private record Connection(GraphCanvas canvas, CircularNode node1, CircularNode node2) {
        public void draw(GraphicsContext gc) {

            gc.setStroke(Color.WHITE);
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

        private void drawArrowhead(GraphicsContext gc, double x, double y, double angle, double arrowSize) {
            double arrowheadAngle = Math.toRadians(30);
            double x1 = x - arrowSize * Math.cos(angle - arrowheadAngle);
            double y1 = y - arrowSize * Math.sin(angle - arrowheadAngle);
            double x2 = x - arrowSize * Math.cos(angle + arrowheadAngle);
            double y2 = y - arrowSize * Math.sin(angle + arrowheadAngle);

            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{x, x1, x2}, new double[]{y, y1, y2}, 3);
        }
    }
}
