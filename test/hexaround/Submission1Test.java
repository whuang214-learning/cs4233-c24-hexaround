package hexaround;

import hexaround.game.*;
import hexaround.game.board.MoveResult;
import hexaround.game.entities.creature.CreatureProperty;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.*;

import static hexaround.game.entities.creature.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    HexAroundFirstSubmission gameManager = null;

    // checks if the creature is placed at the given coordinates
    @Test
    void testPlaceCreature() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        assertEquals(gameManager.placeCreature(GRASSHOPPER, 5, 42).moveResult(), MoveResult.OK);
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(5, 42));
    }

    // checks if moveResult is MOVE_ERROR when the creature is placed at the same coordinates
    @Test
    void testInvalidCreaturePlacement() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(gameManager.placeCreature(GRASSHOPPER, 5, 42).moveResult(), MoveResult.MOVE_ERROR);
    }

    // checks the isOccupied method
    @Test
    void testIsOccupied() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.isOccupied(5, 42));

    }

    // check the hasProperty method
    @Test
    void testHasProperty() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(BUTTERFLY, 5, 42);
        assertTrue(gameManager.hasProperty(5, 42, CreatureProperty.QUEEN));
        assertTrue(gameManager.hasProperty(5, 42, CreatureProperty.WALKING));
        assertFalse(gameManager.hasProperty(5, 42, CreatureProperty.JUMPING));
    }

    // test the can reach method
    @Test
    void testCanReach() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(BUTTERFLY, 5, 42);
        gameManager.placeCreature(BUTTERFLY, 5, 43);
        assertTrue(gameManager.canReach(5, 42, 5, 43));
        assertFalse(gameManager.canReach(5, 42, 5, 44));
    }






}
