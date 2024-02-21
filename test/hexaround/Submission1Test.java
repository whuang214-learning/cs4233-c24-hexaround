package hexaround;

import hexaround.game.*;
import hexaround.game.board.MoveResponse;
import hexaround.game.board.MoveResult;
import hexaround.game.entities.creature.CreatureName;
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

        MoveResponse response = gameManager.placeCreature(GRASSHOPPER, 5, 42);

        assertEquals(response.moveResult(), MoveResult.OK);
        assertEquals(response.message(), "Legal move");
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(5, 42));
    }

    // placeCreature test number 2
    @Test
    void testPlaceCreatureTwo() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        MoveResponse responseOne = gameManager.placeCreature(BUTTERFLY, 5, 5);
        MoveResponse responseTwo = gameManager.placeCreature(GRASSHOPPER, 5, 6);

        assertEquals(responseOne.moveResult(), MoveResult.OK);
        assertEquals(responseOne.message(), "Legal move");
        assertEquals(responseTwo.moveResult(), MoveResult.OK);
        assertEquals(responseTwo.message(), "Legal move");
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
        assertTrue(gameManager.canReach(5, 42, 5, 43));
        assertFalse(gameManager.canReach(5, 42, 5, 44));
    }

    // additional test for canReach
    @Test
    void testCanReachTwo() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 0, 0);
        assertTrue(gameManager.canReach(0, 0, 0, 3));
        assertTrue(gameManager.canReach(0, 0, 1, 2));
    }

    // test moveCreature method
    @Test
    void testMoveCreature() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        gameManager.placeCreature(BUTTERFLY, 5, 43);
        assertEquals(gameManager.moveCreature(GRASSHOPPER, 5, 42, 5, 43).moveResult(), MoveResult.OK);
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(5, 43));
    }






}
