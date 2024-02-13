package hexaround;

import hexaround.game.*;
import hexaround.required.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.game.entities.creature.CreatureName.*;
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
    void testPlaceCreature() {
        assertEquals(gameManager.placeCreature(GRASSHOPPER, 5, 42).moveResult(), MoveResult.OK);
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(5, 42));
    }

    // checks if moveResult is MOVE_ERROR when the creature is placed at the same coordinates
    @Test
    void testInvalidCreaturePlacement() {
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(gameManager.placeCreature(GRASSHOPPER, 5, 42).moveResult(), MoveResult.MOVE_ERROR);
    }

    // checks the isOccupied method
    @Test
    void testIsOccupied() {
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.isOccupied(5, 42));
    }
}
