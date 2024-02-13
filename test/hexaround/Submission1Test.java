package hexaround;

import hexaround.config.*;
import hexaround.game.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.required.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    IHexAround1 gameManager = null;

    @BeforeEach
    void setUp() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);
    }

    // checks if the creature is placed at the given coordinates
    @Test
    void firstTest() throws IOException {
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(GRASSHOPPER,gameManager.getCreatureAt(5, 42));
    }

    // checks the isOccupied method
    @Test
    void isOccupiedTest() throws IOException {
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.isOccupied(5, 42));
    }
}
