package views;

import AdventureController.Controller;
import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Connection;
import AdventureModel.Room;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.AccessibleRole;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Class AdventureGameView.
 * Displays the main game view for the adventure game.
 * Code partially generated in response to comments. GitHub CoPilot, 9 Mar. 2023, https://github.com/features/copilot
 *
 * @author Abigail Yanku
 * @author Themba Dube
 * @version 1.69
 */
public class ViewAdventureEditor {
    /**
     * The main controller for the game.
     */
    Controller controller;
    AdventureGame model; //model of the game //TODO: Remove when save and load are implemented
    /**
     * The stage on which all is rendered.
     */
    Stage stage;
    /**
     * The layout of the game.
     */
    BorderPane layout;
    /**
     * The buttons for the game.
     */
    Button runButton, addGateButton, addObjectButton, addRoomButton, imageButton, visualizeButton;
    /**
     * The labels for the game.
     */
    Label imageLabel;
    /**
     * The text fields for the room.
     */
    TextField nameField;
    /**
     * The text areas for the room.
     */
    TextArea descriptionField;
    /**
     * The checkboxes for the room.
     */
    CheckBox endCheckBox;
    /**
     * The image paths for the room.
     */
    String ImagePath;
    /**
     * The image view for the room.
     */
    ImageView roomImageView;
    /**
     * The scroll panes for the game.
     */
    ScrollPane gatePane, objectsPane, allRooms;
    /**
     * The currently selected room.
     */
    Room currentlySelectedRoom;


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
     * getStage
     * Gets the stage for this view
     * @return the stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * setCurrentlySelectedRoom
     * Sets the currently selected room
     * @param room the room to set
     */
    public void setCurrentlySelectedRoom(Room room) {
        currentlySelectedRoom = room;
        updateRoomView();
    }

    /**
     * setController
     * __________________________
     * Sets the controller for the view
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        if(this.controller != null)
            throw new IllegalStateException("Already attached a controller");
        this.controller = controller;
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // Setting up the stage
        this.stage.setTitle("AdventureEditorPro");
        layout = new BorderPane();

        //-------------------------------------------------------------------------------------------------------------
        //Create File Menu
        Menu fileMenu = new Menu("_File");
        //Create Load Item
        MenuItem loadFile = new MenuItem("_Load...");
        loadFile.setOnAction(e -> this.controller.onLoadRequest());
        fileMenu.getItems().add(loadFile);
        //Create Save Item
        MenuItem saveFile = new MenuItem("_Save...");
        saveFile.setOnAction(e -> this.controller.onSaveRequest());
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
        addRoomButton.setOnAction(e -> {
            controller.addRoom();
        });
        addRoomButton.setAlignment(Pos.TOP_RIGHT);
        roomsLabel.setAlignment(Pos.TOP_LEFT);
        roomViewHbox.getChildren().addAll(roomsLabel, addRoomButton);

        //Create All Rooms Scroll Pane
        allRooms = new ScrollPane();
        allRooms.setContent(createMiniRoomView(List.of()));
        allRooms.setPrefWidth(271);
        allRooms.setPrefHeight(1000);

        // Build Left Pane
        VBox leftPane = new VBox();
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.getChildren().addAll(roomViewHbox, allRooms);

        //-------------------------------------------------------------------------------------------------------------
        //Create Gate Scroll Pane and Label
        gatePane = new ScrollPane();
        gatePane.setContent(createMiniGateView(Map.of()));
        gatePane.setPrefWidth(190);
        gatePane.setPrefHeight(500);
        Label gatesLabel = new Label("Gates to Current Room:  ");
        HBox gatesLabelButton = new HBox();
        //Create Visualize Button
        visualizeButton = new Button("Visualize");
        visualizeButton.setOnAction(e -> {
            this.controller.visualizeGatesFromRoom(currentlySelectedRoom);
        });
        //Add Gates Label and Visualize Button to HBox
        gatesLabelButton.setPadding(new Insets(5, 5, 5, 5));
        gatesLabelButton.getChildren().addAll(gatesLabel, visualizeButton);

        //Create Objects Scroll Pane
        objectsPane = new ScrollPane();
        objectsPane.setContent(createMiniObjectView(List.of()));
        objectsPane.setPrefWidth(190);
        objectsPane.setPrefHeight(400);
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
        this.stage.getIcons().add(new Image("assets/icon.png"));
        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        this.stage.show();

    }

    /**
     * updateRoomView
     * Updates the current room view with an empty room.
     */
    public void updateRoomView() {
        if(currentlySelectedRoom == null) {
            ImageView logo = new ImageView(new Image("assets/icon.png"));
            logo.setFitWidth(500);
            logo.setFitHeight(500);
            this.layout.setCenter(logo);
            return;
        }
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
        nameField.setText(currentlySelectedRoom.getRoomName());
        nameField.textProperty().addListener((observable, old, newVal) -> {
            controller.updateRoomName(currentlySelectedRoom, newVal);
        });
        //Add description label
        Label descriptionLabel = new Label("Room Description:");
        //Add description text field
        descriptionField = new TextArea();
        descriptionField.setPrefWidth(400);
        descriptionField.setPrefHeight(200);
        descriptionField.setPromptText("Enter a Room Description");
        descriptionField.wrapTextProperty().setValue(true);
        descriptionField.setText(currentlySelectedRoom.getUnsanitizedRoomDescription());
        descriptionField.textProperty().addListener((observable, old, newVal) -> {
            controller.updateRoomDescription(currentlySelectedRoom, newVal);
        });

        //Add Default Image View (GridPane 2)
        GridPane roomViewG2 = new GridPane();
        roomViewG2.setPadding(new Insets(10, 10, 10, 10));
        roomViewG2.setVgap(20);
        roomViewG2.setHgap(20);
        roomViewG2.setAlignment(Pos.TOP_LEFT);
        //roomViewG2.gridLinesVisibleProperty().setValue(true);

        //Add Image View

        Image defaultImageFile;
        try {
            File file = new File(model.getDirectoryName() + "/room-images/" + currentlySelectedRoom.getRoomNumber() + ".png");
            defaultImageFile = new Image(new FileInputStream(file));
        } catch(RuntimeException | IOException e) { // image probably not found
            e.printStackTrace();
            defaultImageFile = null;
        }
        roomImageView = new ImageView(defaultImageFile);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(200);
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

        //Add End Check Box
        endCheckBox = new CheckBox("End");
        // Check the room's status and set the checkbox accordingly
        endCheckBox.setSelected(currentlySelectedRoom.getEndStatus());
        endCheckBox.setOnAction(e -> handleEndCheckBox());

        //-------------------------------------------------------------------------------------------------------------

        //Add all elements to GridPanes
        roomViewG1.add(nameLabel, 0, 0);
        roomViewG1.add(nameField, 0, 1);
        roomViewG1.add(descriptionLabel, 0, 2);
        roomViewG1.add(descriptionField, 0, 3);
        roomViewG2.add(imageButtonLabel, 0, 0);
        roomViewG2.add(roomImageView, 0, 1);
        roomViewG3.add(addObjectButton, 1, 0);
        roomViewG3.add(addGateButton, 0, 0);
        roomViewG3.add(endCheckBox, 2, 0);

        //Create New Room View using GridPanes
        GridPane roomView = new GridPane();
        roomView.setPadding(new Insets(10, 10, 10, 10));
        //roomView.gridLinesVisibleProperty().setValue(true);
        roomView.setAlignment(Pos.CENTER);
        roomView.add(roomViewG1, 0, 0);
        roomView.add(roomViewG2, 0, 1);
        roomView.add(roomViewG3, 0, 2);
        
        this.layout.setCenter(roomView);

        updateAllGates(currentlySelectedRoom.getPassages());
        updateAllObjects(currentlySelectedRoom.getObjectsInRoom());
    }

    //-------------------------------------------------------------------------------------------------------------
    // Update Methods Begin
    //-------------------------------------------------------------------------------------------------------------

    /**
     * updateAllRooms
     * Updates the allRooms ScrollPane anytime an edit is made, a room is added, or a room is deleted.
     * @param rooms the rooms to update the view with
     */
    public void updateAllRooms(Collection<Room> rooms) {
        allRooms.setContent(createMiniRoomView(rooms));
    }

    /**
     * updateAllGates
     * Updates the gatePane ScrollPane anytime an edit is made, a gate is added, or a gate is deleted.
     * @param passages the gates to update the view with
     */
    public void updateAllGates(Map<Connection, Room> passages) {
        gatePane.setContent(createMiniGateView(passages));
    }

    /**
     * updateAllObjects
     * Updates the gatePane ScrollPane anytime an edit is made, a gate is added, or a gate is deleted.
     * @param objects the objects to update the view with
     */
    public void updateAllObjects(Collection<AdventureObject> objects){
        objectsPane.setContent(createMiniObjectView(objects));
    }

    //-------------------------------------------------------------------------------------------------------------
    // Create Methods Begin
    //-------------------------------------------------------------------------------------------------------------

    /**
     * createMiniRoomView
     * Creates a mini room view for the current room in the main room view.
     * @param rooms the rooms to create the view for
     */
    private Node createMiniRoomView(Collection<Room> rooms) {
        VBox roomList = new VBox();
        Image trashIcon = new Image("assets/trash_icon.png");

        for(Room room : rooms) {
            //Create First Room Hbox
            HBox miniRoomView = new HBox();

            //Create Delete Button
            ImageView trashIconView = new ImageView(trashIcon);
            trashIconView.setFitWidth(30);
            trashIconView.setFitHeight(30);
            Button deleteButton = new Button();
            deleteButton.setPrefWidth(30);
            deleteButton.setPrefHeight(80);
            deleteButton.setOnAction(e -> controller.deleteRoom(room));
            deleteButton.setGraphic(trashIconView);


            // Create Room Information Vbox
            VBox roomInfo = new VBox();
            roomInfo.setStyle("-fx-background-color: #fefeff;");
            Label roomLabel = new Label(room.getRoomName());
            roomLabel.setStyle("-fx-text-fill: #122435;");
            if (room.getEndStatus()){
                Label endLabel = new Label("This is an End Room.");
                endLabel.setStyle("-fx-text-fill: #122435;");
                roomInfo.getChildren().addAll(roomLabel,endLabel);
            } else if (room.getStartStatus()){
                Label startLabel = new Label("The Start Room.");
                startLabel.setStyle("-fx-text-fill: #122435;");
                roomInfo.getChildren().addAll(roomLabel,startLabel);
            } else {
                roomInfo.getChildren().add(roomLabel);
            }

            roomInfo.setPrefHeight(80);
            roomInfo.setPrefWidth(185);
            roomInfo.setAlignment(Pos.CENTER);
            roomInfo.setBorder(new Border(new BorderStroke(Color.rgb(0, 45, 61), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            roomInfo.setOnMouseClicked(e -> {
                setCurrentlySelectedRoom(room);
            });

            // Add Room Info Vbox to Hbox
            miniRoomView.getChildren().addAll(roomInfo, deleteButton);

            roomList.getChildren().add(miniRoomView);
        }

        return roomList;
    }

    /**
     * createMiniGateView
     * Creates a mini gate view for the current room to another room.
     * @param gates the gates to create the view for (the keys are the gates, the values are the rooms)
     */
    public Node createMiniGateView(Map<Connection, Room> gates) {
        VBox gateList = new VBox();

        Image trashIcon = new Image("assets/trash_icon.png");

        for(Connection gate : gates.keySet()) {
            //Create First Room Hbox
            HBox miniRoomView = new HBox();

            //Create Delete Button
            ImageView trashIconView = new ImageView(trashIcon);
            trashIconView.setFitWidth(30);
            trashIconView.setFitHeight(30);
            Button deleteButton = new Button();
            deleteButton.setPrefWidth(30);
            deleteButton.setPrefHeight(80);
            deleteButton.setOnAction(e -> controller.deleteGateFromRoom(currentlySelectedRoom, gate));
            deleteButton.setGraphic(trashIconView);


            // Create Gate Information Vbox
            VBox GateInfo = new VBox();
            GateInfo.setStyle("-fx-background-color: #fefeff;");
            if (gates.get(gate) != null){
                Label GateLabel = new Label(gate.direction() + " to " + gates.get(gate).getRoomName());
                GateLabel.setStyle("-fx-text-fill: #122435;");
                if (gate.lock() != null){
                    Label GateObject = new Label("With " + gate.lock());
                    GateObject.setStyle("-fx-text-fill: #122435;");
                    GateInfo.getChildren().addAll(GateObject, GateLabel);
                } else {
                    GateInfo.getChildren().add(GateLabel);
                }
            } else {
                return new VBox();
            }

            GateInfo.setPrefHeight(80);
            GateInfo.setPrefWidth(240);
            GateInfo.setAlignment(Pos.CENTER);
            GateInfo.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            GateInfo.setOnMouseClicked(e -> {
                currentlySelectedRoom = gates.get(gate);
                updateRoomView();
            });

            // Add Room Info Vbox to Hbox
            miniRoomView.getChildren().addAll(GateInfo, deleteButton);

            gateList.getChildren().add(miniRoomView);
        }

        return gateList;
    }

    /**
     * createMiniObjectView
     */
    public Node createMiniObjectView(Collection<AdventureObject> objects) {
        GridPane objectsGrid = new GridPane();
        GridPane.setRowSpan(objectsGrid, 3);
        //Create Column Constraints
        int i = 0;
        int j = 0;

        Image trashIcon = new Image("assets/trash_icon.png");

        for (AdventureObject o: objects){
            VBox objectBox = new VBox();
            objectBox.setStyle("-fx-background-color: #fefeff;");
            objectBox.setBorder(new Border(new BorderStroke(Color.rgb(0, 45, 61), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            //Create Image of Object
            Image image;
            try {
                image = new Image(Files.newInputStream(Path.of(controller.getDirectoryName(), "objectImages", o.getName() + ".jpg")));
            } catch(IOException e) {
                e.printStackTrace();
                image = null;
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(148);

            //Create Label and HBox for Delete Button
            HBox labelAndDelete = new HBox();
            labelAndDelete.setPrefWidth(148);
            Label objectLabel = new Label(o.getName());
            objectLabel.setStyle("-fx-text-fill: #122435;");
            objectLabel.setPrefWidth(100);
            objectLabel.setPadding(new Insets(10, 10, 10, 10));

            //Create Delete Button
            ImageView trashIconView = new ImageView(trashIcon);
            trashIconView.setFitWidth(30);
            trashIconView.setFitHeight(30);
            Button deleteButton = new Button();
            deleteButton.setPrefWidth(30);
            deleteButton.setPrefHeight(30);
            deleteButton.setAlignment(Pos.TOP_RIGHT);
            deleteButton.setPadding(new Insets(10, 10, 10, 10));
            deleteButton.setOnAction(e -> controller.deleteObjectFromRoom(currentlySelectedRoom, o));
            deleteButton.setGraphic(trashIconView);

            //Add Label and Delete Button to HBox
            labelAndDelete.getChildren().addAll(objectLabel, deleteButton);
            objectBox.getChildren().addAll(imageView, labelAndDelete);
            objectBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            //Add Object Box to GridPane
            objectsGrid.add(objectBox, j, i);

            //Increment i and j
            if (j == 1){
                i++;
                j = 0;
            } else{
                j++;
            }
        }
        return objectsGrid;
    }

    //-------------------------------------------------------------------------------------------------------------

    /**
     * forceUncheckEnd
     * Forces the end checkbox to be unchecked
     */
    public void forceUncheckEnd() {
        this.endCheckBox.setSelected(false);
    }

    public Room getCurrentlySelectedRoom() {return this.currentlySelectedRoom;}

    /**
     * makeButtonAccessible
     * __________________________
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

    //------------------------------------------------------------------------------------------------------------------
    // Handle Methods Begin
    //------------------------------------------------------------------------------------------------------------------

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
        new HelpView();
    }

    /**
     * handleAddObjectButton
     */
    private void handleAddObjectButton() {
        new CreateObject(controller, currentlySelectedRoom);
    }

    /**
     * handleAddGateButton
     */
    private void handleAddGateButton() {
        new CreateGate(controller, currentlySelectedRoom);
    }

    /**
     * handleAddImage
     */
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.getName().endsWith(".png")) {
            ImagePath = selectedFile.getAbsolutePath();
            String dest = "room-images";
            String name = currentlySelectedRoom.getRoomNumber() + ".png";
            controller.updateImage(selectedFile, dest, name);
        } else {
            Dialogs.showDialogAndWait(Alert.AlertType.ERROR, "Only .png files are accepted");
        }
    }

    /**
     * handleEndCheckBox
     */
    private void handleEndCheckBox() {
        //Update the end status of the room in the backend
        controller.updateEndStatus(currentlySelectedRoom, endCheckBox.isSelected());
    }
}
