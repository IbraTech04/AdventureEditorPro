package views;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import AdventureModel.AdventureGame;
import AdventureModel.AdventureLoader;
import javafx.stage.Modality;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Class CreateGate. Display that allows users to create Gates/Passages to Rooms.
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 */

public class CreateGate {
    private ViewAdventureEditor adventureEditor;
    private MenuButton selectRoomButton;
    private ArrayList<String> selectRoomList;
    private MenuButton selectDirectionButton;
    private MenuButton selectForcedButton;
    ActionEvent even;

    private ListView<String> GameList;
    private String filename = null;

    public CreateGate(ViewAdventureEditor adventureEditor) {
        this.adventureEditor = adventureEditor;


    }
    public void choose_room (ActionEvent even){


    }

}
