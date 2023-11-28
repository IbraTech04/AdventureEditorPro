package gptfx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX Abstraction Layer which renders a PassageCycle
 * @author Ibrahim Chehab, ChatGPT
 */
public class GraphCanvas extends Canvas {
    private List<CircularNode> nodes;
    private List<Connection> connections;

    public GraphCanvas(double width, double height) {
        super(width, height);
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

    public void addNode(double x, double y, double radius) {
        CircularNode node = new CircularNode(x, y, radius);
        nodes.add(node);
        redrawCanvas();
    }

    public void connectNodes(int index1, int index2) {
        if (index1 >= 0 && index1 < nodes.size() && index2 >= 0 && index2 < nodes.size()) {
            connections.add(new Connection(nodes.get(index1), nodes.get(index2)));
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        for (Connection connection : connections) {
            connection.draw(gc);
        }

        for (CircularNode node : nodes) {
            node.draw(gc);
        }
    }

    private static class CircularNode {
        private double x;
        private double y;
        private double radius;

        public CircularNode(double x, double y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public void draw(GraphicsContext gc) {
            gc.setFill(Color.BLUE);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }

    private static class Connection {
        private CircularNode node1;
        private CircularNode node2;

        public Connection(CircularNode node1, CircularNode node2) {
            this.node1 = node1;
            this.node2 = node2;
        }

        public void draw(GraphicsContext gc) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeLine(node1.x, node1.y, node2.x, node2.y);
        }
    }
}
