package AdventureModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;

public class AdventureSaver {
    private AdventureGame game; //the game to store
    private String adventureName; //the name of the adventure

    /**
     * Adventure Saver Constructor
     * __________________________
     * Initializes attributes
     * @param game the game that is loaded
     * @param directoryName the directory in which game files will be stored
     */
    public AdventureSaver(AdventureGame game, String directoryName) {
        this.game = game;
        this.adventureName = "Games/" + directoryName;
    }

    public void saveGame() throws IOException {
        // Make the game folder if it doesn't exist
        if(!Files.isDirectory(Path.of(this.adventureName)))
            Files.createDirectory(Path.of(this.adventureName));

        // Do the actual saving
        saveRooms();
        saveObjects();
        saveSynonyms();
        saveHelp();
    }

    private void saveRooms() throws IOException {
        String roomFileName = this.adventureName + "/rooms.txt";
        BufferedWriter buff = new BufferedWriter(new FileWriter(roomFileName));

        for(var entry : this.game.getRooms().entrySet()) {
            var room = entry.getValue();

            // write room ID
            buff.write(entry.getKey() + "\n");

            // write room name
            buff.write(room.getRoomName() + "\n");
            // write room description & separator
            // we remove the last newline to make it match the originally loaded file
            String desc = room.getUnsanitizedRoomDescription();

            // Remove trailing newlines
            while(desc.endsWith("\n")){
                desc = desc.substring(0, desc.lastIndexOf('\n'));
            }
            desc += "\n";
            buff.write(desc);
            buff.write(AdventureLoader.DESCRIPTION_SEPARATOR + "\n");

            // write connections
            for(var passageEntry : room.getPassages().entrySet()) {
                String blockedSuffix;
                var lock = passageEntry.getKey().lock();
                if(lock != null) {
                    blockedSuffix = "/" + lock;
                } else {
                    blockedSuffix = "";
                }
                var destination = passageEntry.getValue();
                var destId = destination != null ? destination.getRoomNumber() : 0;
                buff.write(String.format("%-10s %d%s\n", passageEntry.getKey().direction(), destId, blockedSuffix));
            }
            // write spacer
            buff.write("\n");
        }

        buff.close();
    }

    private void saveObjects() throws IOException {
        String objectFileName = this.adventureName + "/objects.txt";
        BufferedWriter buff = new BufferedWriter(new FileWriter(objectFileName));

        var objectsToWrite = this.game.getRooms().entrySet().stream()
                .flatMap(e -> e.getValue().objectsInRoom.stream()
                        .map(o -> new AbstractMap.SimpleEntry<>(e.getKey(), o)))
                .iterator();

        while(objectsToWrite.hasNext()) {
            var entry = objectsToWrite.next();
            var object = entry.getValue();
            buff.write(object.getName() + "\n");
            buff.write(object.getDescription() + "\n");
            buff.write(entry.getKey() + "\n");
            // only add separator if there is more to write
            if (objectsToWrite.hasNext()) {
                buff.write("\n");
            }
        }

        buff.close();
    }

    private void saveSynonyms() throws IOException {
        String synonymFileName = this.adventureName + "/synonyms.txt";
        BufferedWriter buff = new BufferedWriter(new FileWriter(synonymFileName));

        for(var entry : this.game.getSynonyms().entrySet()) {
            buff.write(entry.getKey() + "=" + entry.getValue() + "\n");
        }

        buff.close();
    }

    private void saveHelp() throws IOException {
        String synonymFileName = this.adventureName + "/help.txt";
        BufferedWriter buff = new BufferedWriter(new FileWriter(synonymFileName));

        buff.write(this.game.getInstructions());

        buff.close();
    }
}
