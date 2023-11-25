package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import javafx.stage.FileChooser;
import jdk.jfr.Event;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class AdventureGameView.
 * //TODO: Update all print statements to load their respective popups
 * //TODO: Update all buttons to have ARIA standards
 */
public class ViewAdventureEditor {

    AdventureGame model; //model of the game //TODO: Remove when save and load are implemented
    Stage stage; //stage on which all is rendered
    BorderPane layout; //layout of the stage
    Button runButton, editButton, deleteButton, addGateButton, addObjectButton, addRoomButton, imageButton, visualizeButton;
    Label imageLabel;
    TextField nameField;
    TextArea descriptionField;
    CheckBox forcedCheckBox, startCheckBox, endCheckBox;
    ScrollPane allRooms;
    String ImagePath, RoomName, RoomDescription;
    Boolean isStart, isEnd, isForced;
    ImageView roomImageView;
    ArrayList<HBox> roomsList;


    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public ViewAdventureEditor(AdventureGame model, Stage stage) {
        this.model = model; //TODO: Remove when save and load are implemented
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // Setting up the stage
        this.stage.setTitle("Adventure Game Creator");
        layout = new BorderPane();

        //-------------------------------------------------------------------------------------------------------------
        //Create File Menu
        Menu fileMenu = new Menu("_File");
        //Create Load Item
        MenuItem loadFile = new MenuItem("_Load...");
        loadFile.setOnAction(e -> handleLoadFile());
        fileMenu.getItems().add(loadFile);
        //Create Save Item
        MenuItem saveFile = new MenuItem("_Save...");
        saveFile.setOnAction(e -> handleSaveFile());
        fileMenu.getItems().add(saveFile);
        fileMenu.getItems().add(new SeparatorMenuItem());
        //Create Exit Item
        MenuItem exitFile = new MenuItem("_Quit");
        exitFile.setOnAction(e -> handleExit());
        fileMenu.getItems().add(exitFile);
        //Create Help Menu
        Menu helpMenu = new Menu("_Help");
        //Create Help Item
        MenuItem helpItem = new MenuItem("_Help...");
        helpItem.setOnAction(e -> handleHelp());
        helpMenu.getItems().add(helpItem);

        //Add Menu's to Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        //-------------------------------------------------------------------------------------------------------------
        //Create Room View
        updateRoomView();

        //-------------------------------------------------------------------------------------------------------------
        //Create New Room Vbox
        HBox roomViewHbox = new HBox();
        roomViewHbox.setPadding(new Insets(5, 5, 5, 5));
        roomViewHbox.setPrefWidth(250);
        roomViewHbox.setPrefHeight(20);
        Label roomsLabel = new Label("All Rooms:  ");
        addRoomButton = new Button("Add Room");
        addRoomButton.setOnAction(e -> handleAddRoomButton());
        addRoomButton.setAlignment(Pos.TOP_RIGHT);
        roomsLabel.setAlignment(Pos.TOP_LEFT);
        roomViewHbox.getChildren().addAll(roomsLabel, addRoomButton);

        //Create All Rooms Scroll Pane
        allRooms = new ScrollPane();
        allRooms.setContent(createMiniRoomView());
        allRooms.setPrefWidth(210);
        allRooms.setPrefHeight(1000);

        // Build Left Pane
        VBox leftPane = new VBox();
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.getChildren().addAll(roomViewHbox, allRooms);

        //-------------------------------------------------------------------------------------------------------------
        //Create Gate Scroll Pane and Label
        ScrollPane gatePane = new ScrollPane();
        gatePane.setPrefWidth(190);
        gatePane.setPrefHeight(500);
        Label gatesLabel = new Label("Gates to Current Room:  ");
        HBox gatesLabelButton = new HBox();
        //Create Visualize Button
        visualizeButton = new Button("Visualize");
        visualizeButton.setOnAction(e -> handleVisualize());
        //Add Gates Label and Visualize Button to HBox
        gatesLabelButton.setPadding(new Insets(5, 5, 5, 5));
        gatesLabelButton.getChildren().addAll(gatesLabel, visualizeButton);

        //Create Objects Scroll Pane
        ScrollPane objectsPane = new ScrollPane();
        objectsPane.setPrefWidth(190);
        objectsPane.setPrefHeight(400);
        GridPane objectsGrid = new GridPane();
        GridPane.setColumnSpan(objectsGrid, 3);
        objectsPane.setContent(objectsGrid);
        Label objectsLabel = new Label("Objects in Current Room:");

        // Build Right Pane
        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(10, 10, 10, 10));
        rightPane.getChildren().addAll(gatesLabelButton, gatePane, objectsLabel, objectsPane);

        //-------------------------------------------------------------------------------------------------------------
        // Render everything
        layout.setTop(menuBar);
        layout.setLeft(leftPane);
        layout.setRight(rightPane);
        var scene = new Scene( layout , 1100, 800);
        scene.getStylesheets().add("views/styles/HighContrast.css");
        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        this.stage.show();

    }

    /**
     * updateRoomView
     * Updates the current room view with an empty room. //TODO: Integrate loading of existing room
     */
    public void updateRoomView() {
        //Create Text Box's (GridPane 1)
        GridPane roomViewG1 = new GridPane();
        roomViewG1.setPadding(new Insets(10, 10, 10, 10));
        roomViewG1.setVgap(10);
        roomViewG1.setHgap(10);
        roomViewG1.setAlignment(Pos.CENTER);
        //roomViewG1.gridLinesVisibleProperty().setValue(true);

        //Add name label
        Label nameLabel = new Label("Room Name:");
        //Add name text field
        nameField = new TextField();
        nameField.setPrefWidth(400);
        nameField.setPromptText("Choose a Room Name");
        nameField.setOnKeyPressed(e -> handleNameField(e));
        //Add description label
        Label descriptionLabel = new Label("Room Description:");
        //Add description text field
        descriptionField = new TextArea();
        descriptionField.setPrefWidth(400);
        descriptionField.setPrefHeight(200);
        descriptionField.setPromptText("Enter a Room Description");
        descriptionField.wrapTextProperty().setValue(true);
        descriptionField.setOnKeyPressed(e -> handleDescriptionField(e));

        //Add Default Image View (GridPane 2)
        GridPane roomViewG2 = new GridPane();
        roomViewG2.setPadding(new Insets(10, 10, 10, 10));
        roomViewG2.setVgap(20);
        roomViewG2.setHgap(20);
        roomViewG2.setAlignment(Pos.TOP_LEFT);
        //roomViewG2.gridLinesVisibleProperty().setValue(true);

        //Add Image View
        Image defaultImageFile = new Image("assets/ahmed.jpg");
        roomImageView = new ImageView(defaultImageFile);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(200); //TODO: Make this change according to image size
        HBox imageButtonLabel = new HBox();
        imageLabel = new Label("");
        imageButton = new Button("Add Image");
        imageButton.setOnAction(e -> handleAddImage());
        imageButtonLabel.getChildren().addAll(imageButton, imageLabel);

        //Create Options Box's (GridPane 3)
        GridPane roomViewG3 = new GridPane();
        roomViewG3.setPadding(new Insets(10, 10, 10, 10));
        roomViewG3.setVgap(20);
        roomViewG3.setHgap(20);
        roomViewG3.setAlignment(Pos.TOP_LEFT);
        //roomViewG3.gridLinesVisibleProperty().setValue(true);

        //Add Object addition button
        addObjectButton = new Button("Add Object");
        addObjectButton.setOnAction(e -> handleAddObjectButton());
        //Add Gate addition button
        addGateButton = new Button("Add Gate");
        addGateButton.setOnAction(e -> handleAddGateButton());

        //Add Forced Check Box
        forcedCheckBox = new CheckBox("Is Forced?");
        isForced = true;
        forcedCheckBox.setOnAction(e -> handleForcedCheckBox());

        //Add Start Check Box
        startCheckBox = new CheckBox("Start");
        startCheckBox.setSelected(true);
        isStart = true;
        startCheckBox.setOnAction(e -> handleStartCheckBox());

        //Add End Check Box
        endCheckBox = new CheckBox("End");
        isEnd = true;
        endCheckBox.setOnAction(e -> handleEndCheckBox());

        //-------------------------------------------------------------------------------------------------------------
        //Create Run Button
        HBox runButtonBox = new HBox();
        runButton = new Button("Run Game!");
        runButton.setOnAction(e -> handleRun());
        runButtonBox.getChildren().add(runButton);
        runButtonBox.setAlignment(Pos.CENTER);
        runButtonBox.setPadding(new Insets(10, 10, 10, 10));

        //Add all elements to GridPanes
        roomViewG1.add(nameLabel, 0, 0);
        roomViewG1.add(nameField, 0, 1);
        roomViewG1.add(descriptionLabel, 0, 2);
        roomViewG1.add(descriptionField, 0, 3);
        roomViewG2.add(imageButtonLabel, 0, 0);
        roomViewG2.add(roomImageView, 0, 1);
        roomViewG3.add(addObjectButton, 1, 0);
        roomViewG3.add(addGateButton, 0, 0);
        roomViewG3.add(forcedCheckBox, 0, 1);
        roomViewG3.add(startCheckBox, 1, 1);
        roomViewG3.add(endCheckBox, 2, 1);

        //Create New Room View using GridPanes
        GridPane roomView = new GridPane();
        roomView.setPadding(new Insets(10, 10, 10, 10));
        //roomView.gridLinesVisibleProperty().setValue(true);
        roomView.setAlignment(Pos.CENTER);
        roomView.add(roomViewG1, 0, 0);
        roomView.add(roomViewG2, 0, 1);
        roomView.add(roomViewG3, 0, 2);
        roomView.add(runButtonBox, 0, 3);
        
        this.layout.setCenter(roomView);
    }

    /**
     * updateAllRooms
     * Updates the allRooms ScrollPane anytime an edit is made, a room is added, or a room is deleted.
     */
    public void updateAllRooms() { //TODO: Integrate this method

    }

    /**
     * updateAllRooms
     * Updates the gatePane ScrollPane anytime an edit is made, a gate is added, or a gate is deleted.
     */
    public void updateGates() { //TODO: Integrate this method

    }


    /**
     * createMiniRoomView
     * Creates a mini room view for the current room in the main room view.
     */
    public HBox createMiniRoomView() {
        //Create First Room Hbox
        HBox miniRoomView = new HBox();

        //Add Delete and Edit Buttons \\TODO: Allow these buttons to edit and delete a specific room
        VBox deleteEditButtons = new VBox();

        //Create Delete Button
        Image trashIcon = new Image("assets/trash_icon.png");
        ImageView trashIconView = new ImageView(trashIcon);
        trashIconView.setFitWidth(30);
        trashIconView.setFitHeight(30);
        deleteButton = new Button();
        deleteButton.setOnAction(e -> handleDelete());
        deleteButton.setGraphic(trashIconView);

        //Create Edit Button
        Image editIcon = new Image("assets/edit_icon.png");
        ImageView editIconView = new ImageView(editIcon);
        editIconView.setFitWidth(30);
        editIconView.setFitHeight(30);
        editButton = new Button();
        editButton.setOnAction(e -> handleEdit());
        editButton.setGraphic(editIconView);

        deleteEditButtons.getChildren().addAll(deleteButton, editButton);


        // Create Room Information Vbox
        VBox firstRoomInfo = new VBox();
        if (this.RoomName == null) { //If the room has no name, set the name to "Unnamed Room"
            this.RoomName = "Unnamed Room";
        }
        Label firstRoomLabel = new Label(RoomName);
        Label startLabel = new Label("Starting Room");
        Label endLabel = new Label("Ending Room");
        Label forcedLabel = new Label("Forced Room");
        firstRoomInfo.getChildren().add(firstRoomLabel);

        if (isStart) {
            firstRoomInfo.getChildren().add(startLabel); //If the room is the start room, add the start label
        } else if (isEnd) {
            firstRoomInfo.getChildren().add(endLabel); //If the room is the end room, add the end label
        } else {
            firstRoomInfo.getChildren().add(forcedLabel); //If the room is a forced room, add the forced label
        }

        firstRoomInfo.setPrefHeight(60);
        firstRoomInfo.setPrefWidth(190);
        firstRoomInfo.setAlignment(Pos.CENTER);
        firstRoomInfo.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Add Room Info Vbox to Hbox
        miniRoomView.getChildren().addAll(firstRoomInfo, deleteEditButtons);
        return miniRoomView;
    }

    /**
     * createMiniGateView
     * Creates a mini gate view for the current room to another room.
     */
    public HBox createMiniGateView() { //TODO: Implement this method
        return new HBox();
    }



    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }


    /**
     * handleNameField
     * @param e the key event
     */
    private void handleNameField(KeyEvent e) { //TODO: Integrate live updating of mini room view
        if (e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.TAB)){
            this.RoomName = this.nameField.getText();
            this.descriptionField.requestFocus();
            System.out.println("Name Entered");
        }

    }

    /**
     * handleDescriptionField
     * @param e the key event
     */
    private void handleDescriptionField(KeyEvent e) { //TODO: Integrate live updating of mini room view
        if (e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.TAB)){
            this.RoomDescription = this.descriptionField.getText();
            this.addGateButton.requestFocus();
            System.out.println("Description Entered");
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // Handle Methods Begin

    /**
     * handleLoadFile
     */
    private void handleLoadFile() {
        LoadView loadView = new LoadView(this);
        System.out.println("Load File Button Pressed");
    }

    /**
     * handleSaveFile
     */
    private void handleSaveFile() {
        SaveView saveView = new SaveView(this);
        System.out.println("Save File Button Pressed");
    }

    /**
     * handleExit
     */
    private void handleExit() {
        Platform.exit();
    }

    /**
     * handleHelp
     */
    private void handleHelp() {
        System.out.println("Help Button Pressed");
    }

    /**
     * handleAddRoomButton
     */
    private void handleAddRoomButton() {
        System.out.println("Add Room Button Pressed");
    }

    /**
     * handleVisualize
     */
    private void handleVisualize() {
        System.out.println("Visualize Button Pressed");
    }

    /**
     * handleDelete
     */
    private void handleDelete() {
        System.out.println("Delete Button Pressed");
    }

    /**
     * handleEdit
     */
    private void handleEdit() {
        System.out.println("Edit Button Pressed");
    }

    /**
     * handleRun
     */
    private void handleRun() {
        System.out.println("Run Button Pressed");
    }

    /**
     * handleAddObjectButton
     */
    private void handleAddObjectButton() {
        System.out.println("Add Object Button Pressed");
    }

    /**
     * handleAddGateButton
     */
    private void handleAddGateButton() {
        System.out.println("Add Gate Button Pressed");
    }

    /**
     * handleAddImage
     */
    private void handleAddImage() {
        File file = new FileChooser().showOpenDialog(stage);
        if (file != null) {
            ImagePath = file.getAbsolutePath();
            if (ImagePath.endsWith(".png") || ImagePath.endsWith(".jpg")) {
                imageLabel.setText("");
                Image roomImageFile = new Image("file:///" + ImagePath);
                roomImageView.setImage(roomImageFile);
            } else {
                imageLabel.setText("  File must be of type .png or .jpg");
            }
        }

    }

    /**
     * handleForcedCheckBox
     */
    private void handleForcedCheckBox() { //TODO: Integrate live updating of mini room view
        if (this.forcedCheckBox.isSelected()){
            this.isForced = true;
        } else {
            this.isForced = false;
        }
        System.out.println("Room Forced is " + this.isForced);
    }

    /**
     * handleStartCheckBox
     */
    private void handleStartCheckBox() { //TODO: Integrate live updating of mini room view
        if (this.startCheckBox.isSelected()){
            this.isStart = true;
        } else {
            this.isStart = false;
        }
        System.out.println("Room Start is " + this.isStart);
    }

    /**
     * handleEndCheckBox
     */
    private void handleEndCheckBox() { //TODO: Integrate live updating of mini room view
        if (this.endCheckBox.isSelected()){
            this.isEnd = true;
        } else {
            this.isEnd = false;
        }
        System.out.println("Room End is " + this.isEnd);
    }


}