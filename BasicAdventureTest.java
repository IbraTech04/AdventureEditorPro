
import java.io.IOException;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureSaver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    void testLoadAndSave() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        AdventureSaver saver = new AdventureSaver(game, "TinyGame_Saved");
        saver.saveGame();
        // TODO: diff saved game against original game automatically
    }

}
