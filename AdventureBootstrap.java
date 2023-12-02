import AdventureController.Controller;
import AdventureModel.AdventureGame;
import javafx.stage.Stage;
import views.ViewAdventureEditor;

public class AdventureBootstrap {
    public static void loadAndDisplayGame(String gameName) {
        AdventureGame model = new AdventureGame(gameName);
        Stage stage = new Stage();
        stage.setTitle("Visualizer");
        stage.show();
        ViewAdventureEditor view = new ViewAdventureEditor(model, stage);

        // Controller constructor has side effects
        new Controller(model, view);
    }
}
