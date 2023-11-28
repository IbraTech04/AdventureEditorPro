package views;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class WelcomeView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)  {
        // Load the FXML file called WelcomeView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // Set the icon
        primaryStage.getIcons().add(new Image("assets/icon.png"));
        // Set the title
        primaryStage.setTitle("AdventureEditorPro");
        primaryStage.setScene(new Scene(root));
        // Disable Resizing
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
