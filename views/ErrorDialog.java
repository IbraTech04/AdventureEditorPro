package views;

import javafx.scene.control.Alert;

public class ErrorDialog {
    public static void showAndWait(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
