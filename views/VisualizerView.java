package views;


import AdventureModel.Room;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import gptfx.GraphCanvas;

public class VisualizerView {
    private GraphCanvas canvas;
    private Stage stage;

    public VisualizerView(Room startNode) {
        this.stage = new Stage();
        stage.setTitle("Visualizer");
        stage.show();

        canvas = new GraphCanvas(stage.getWidth(), stage.getHeight());
        stage.setScene(new Scene(new VBox(canvas))); // "Why do I have to give it a VBox with one child?" - Themba
    }
}
