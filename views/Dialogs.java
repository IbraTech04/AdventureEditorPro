package views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Dialogs {
    public static ButtonType showDialogAndWait(Alert.AlertType type, String content) {
        return showDialogAndWait(type, null, content);
    }

    public static ButtonType showDialogAndWait(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(Character.toUpperCase(type.name().charAt(0)) + type.name().substring(1).toLowerCase());
        // Use the alert type name as the title
        if(header != null) {
            alert.setHeaderText(header);
        }
        alert.setContentText(content);

        // Set the owner of the alert to the primary stage (if available)
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Make the error alert always on top

        alert.showAndWait();

        return alert.getResult();
    }
}
