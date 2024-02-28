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

    // checks not connected colony error for placeCreature
    @Test
    void testPlaceCreatureConnectedColonyError() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 0, 0);
        assertEquals(gameManager.placeCreature(GRASSHOPPER, 0, 2).moveResult(), MoveResult.MOVE_ERROR);
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

        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(BUTTERFLY, 0, 0);
        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 0, 1, 0, -1);
        assertEquals(MoveResult.OK, afterMove.moveResult());
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(0, -1));
    }

    // testing for move creatures with more creatures
    @Test
    void testMoreMovingCreatureFail() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(BUTTERFLY, 0, 0);
        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, -1, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 2, -2);

        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 1, -1, 1, 1);
        assertEquals(MoveResult.MOVE_ERROR, afterMove.moveResult());
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(1, -1));
    }

    // Positive test for moveCreature
    @Test
    void testMoreMovingCreatureSuccess() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(BUTTERFLY, 0, 0);
        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, -1, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 2, -2);

        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 2, -2, 1, 1);
        assertEquals(MoveResult.OK, afterMove.moveResult());
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(1, 1));
    }

    @Test
    void testMoveCreatureToOccupiedHex() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 0, 0);
        gameManager.placeCreature(BUTTERFLY, 0, 1);
        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 0, 0, 0, 1);

        assertEquals(MoveResult.MOVE_ERROR, afterMove.moveResult());
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(0, 0));
        assertEquals(BUTTERFLY, gameManager.getCreatureAt(0, 1));
    }

    @Test
    void testMoveCreatureToNonAdjacentHex() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        gameManager.placeCreature(GRASSHOPPER, 0, 0);
        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 0, 0, 2, 2);

        assertEquals(MoveResult.MOVE_ERROR, afterMove.moveResult());
        assertEquals(GRASSHOPPER, gameManager.getCreatureAt(0, 0));
        assertNull(gameManager.getCreatureAt(2, 2));
    }

    @Test
    void testMoveCreatureWithoutPlacingFirst() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);

        MoveResponse afterMove = gameManager.moveCreature(GRASSHOPPER, 0, 0, 0, 1);

        assertEquals(MoveResult.MOVE_ERROR, afterMove.moveResult());
        assertNull(gameManager.getCreatureAt(0, 0));
        assertNull(gameManager.getCreatureAt(0, 1));
    }


    // test for win condition
    @Test
    void testSurroundedWinConditionRedOne() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);
        gameManager.placeCreature(BUTTERFLY, 0, 0); //blue
        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 0, -1);
        gameManager.placeCreature(GRASSHOPPER, -1, 0);

        MoveResponse lastMove = gameManager.placeCreature(GRASSHOPPER, -1, 1);
        assertEquals(MoveResult.RED_WON, lastMove.moveResult());
    }

    // test for red win condition from moveCreature
    @Test
    void testSurroundedWinConditionRedTwo() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);
        gameManager.placeCreature(BUTTERFLY, 0, 0); //blue
        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 0, -1);
        gameManager.placeCreature(GRASSHOPPER, -1, 0);
        gameManager.placeCreature(GRASSHOPPER, -1, 2);

        MoveResponse lastMove = gameManager.moveCreature(GRASSHOPPER, -1, 2, -1, 1);
        assertEquals(MoveResult.RED_WON, lastMove.moveResult());
    }

    @Test
    void testSurroundedWinConditionBlue() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);
        gameManager.placeCreature(BUTTERFLY, 0, 1); // blue
        gameManager.placeCreature(BUTTERFLY, 0, 0); // red butter in the middle
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 0, -1);
        gameManager.placeCreature(GRASSHOPPER, -1, 0);

        MoveResponse lastMove = gameManager.placeCreature(GRASSHOPPER, -1, 1);
        assertEquals(MoveResult.BLUE_WON, lastMove.moveResult());
    }

    // test for blue win condition from moveCreature
    @Test
    void testSurroundedWinConditionBlueTwo() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        IHexAround1 gameManager = HexAroundGameBuilder.buildGameManager(hgcFile);
        gameManager.placeCreature(BUTTERFLY, 0, 1); // blue
        gameManager.placeCreature(BUTTERFLY, 0, 0); // red butter in the middle
        gameManager.placeCreature(GRASSHOPPER, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, 1, -1);
        gameManager.placeCreature(GRASSHOPPER, 0, -1);
        gameManager.placeCreature(GRASSHOPPER, -1, 0);
        gameManager.placeCreature(GRASSHOPPER, -1, 2);

        MoveResponse lastMove = gameManager.moveCreature(GRASSHOPPER, -1, 2, -1, 1);
        assertEquals(MoveResult.BLUE_WON, lastMove.moveResult());
    }
}
