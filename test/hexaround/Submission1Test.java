package hexaround;

import hexaround.game.*;
import hexaround.game.board.move.MoveResponse;
import hexaround.game.entities.creature.CreatureProperty;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.game.entities.creature.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;
import static hexaround.game.board.move.MoveResult.*;

public class Submission1Test {
    String hgcTest = "testConfigurations/SecondConfiguration.hgc";
    String hgcTestTwo = "testConfigurations/ThirdConfiguration.hgc";

    HexAroundFirstSubmission game1;
    HexAroundFirstSubmission game2;

    public Submission1Test() throws IOException {
        this.game1 = (HexAroundFirstSubmission) HexAroundGameBuilder.buildGameManager(hgcTest);
        this.game2 = (HexAroundFirstSubmission) HexAroundGameBuilder.buildGameManager(hgcTestTwo);
    }

    @Test
    void testPlaceAndGetCreature() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 5, 42));

        assertEquals(CRAB, this.game1.getCreatureAt(5, 42));
    }

    @Test
    void testGetCreatureEmpty() {
        assertNull(this.game1.getCreatureAt(0, 0));
    }

    @Test
    void testPieceHasProperty() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));

        assertTrue(this.game1.hasProperty(0, 0, CreatureProperty.JUMPING));
    }

    @Test
    void testEmptyHasProperty() {
        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.WALKING));
    }

    @Test
    void testPieceNotHasProperty() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));

        assertFalse(this.game1.hasProperty(0, 0, CreatureProperty.FLYING));
    }

    @Test
    void testYesIsOccupied() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertTrue(this.game1.isOccupied(0, 0));
    }

    @Test
    void testNoIsOccupied() {
        assertFalse(this.game1.isOccupied(0, 0));
    }

    @Test
    void testYesCanReach() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertTrue(this.game1.canReach(0, 0, 0, 1));
    }

    @Test
    void testNoCanReach() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertFalse(this.game1.canReach(0, 0, 0, 2));
    }

    @Test
    void testEmptyCanReach() {
        assertFalse(this.game1.canReach(0, 0, 0, 0));
    }

    @Test
    void testTrackPlayerTurn() {
        assertEquals(0, this.game1.getMoves());

        // Place creature.
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(1, this.game1.getMoves());

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 1));
        assertEquals(2, this.game1.getMoves());

        // Move creature.
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
        assertEquals(3, this.game1.getMoves());

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1));
        assertEquals(4, this.game1.getMoves());
    }

    @Test
    void testTrackTeam() {
        assertTrue(this.game1.getTeam());

        // Place creature.
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertFalse(this.game1.getTeam());

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 1));
        assertTrue(this.game1.getTeam());

        // Move creature.
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
        assertFalse(this.game1.getTeam());

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 1, 1, 1));
        assertTrue(this.game1.getTeam());
    }

    @Test
    void testFirstMoveLegal() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalOccupied() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED,
                this.game1.placeCreature(BUTTERFLY, 0, 0));
    }

    @Test
    void testSecondMoveIllegalNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED,
                this.game1.placeCreature(BUTTERFLY, 2, 0));
    }

    @Test
    void testSecondMoveLegal() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));
    }

    @Test
    void testButterflyRoundFourRequired() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(MoveResponse.MOVE_ERROR_BUTTERFLY_NOT_PLACED,
                this.game1.placeCreature(CRAB, -3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -3, 0));
    }

    @Test
    void testButterflyKamikazeRoundFourRequired() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(TURTLE, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(TURTLE, 0, -1, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_BUTTERFLY_NOT_PLACED,
                this.game2.placeCreature(CRAB, 3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 3, 0));
    }

    @Test
    void testPlaceNextToEnemyPieceIllegal() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_ADJACENT_TO_OPPONENT,
                this.game1.placeCreature(CRAB, -2, 0));

    }

    @Test
    void testMoveButterflyClose() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 1));
    }

    @Test
    void testMoveButterflyFar() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_TOO_FAR, this.game1.moveCreature(BUTTERFLY, 0, 0, 2, 0));
    }

    @Test
    void testMoveButterflyOnTop() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, 0));
    }

    @Test
    void testMoveButterflyOnFullTile() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 0, -1, 1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 2, 0, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_FULL_TILE, this.game1.moveCreature(BUTTERFLY, 0, 0, 1, -1));
    }

    @Test
    void testMoveButterflyDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(BUTTERFLY, 0, 0, -1, 1));
    }

    @Test
    void testMoveButterflyNotDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_NO_LEGAL_PATH, this.game1.moveCreature(BUTTERFLY, 0, 0, -1, 1));
    }

    @Test
    void testMoveWalkingNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(CRAB, 0, 0, 0, 2));
    }

    @Test
    void testMoveWalkingTooFar() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_TOO_FAR, this.game1.moveCreature(CRAB, -1, 0, 3, -1));
    }

    @Test
    void testMoveWalkingOnTopNotIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING, this.game1.moveCreature(CRAB, -1, 0, 1, 0));
    }

    @Test
    void testMoveWalkingOnTopIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
    }

    @Test
    void testNoMatchingPiece() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NO_MATCHING_CREATURE, this.game1.moveCreature(BUTTERFLY, -1, 0, 1, 0));
    }

    @Test
    void testMoveWalkingMiddleNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 3, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -2));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 4, -2));

        assertEquals(MoveResponse.MOVE_ERROR_NO_LEGAL_PATH, this.game1.moveCreature(CRAB, 0, -2, 3, -2));
    }

    @Test
    void testMoveWalkingInPlaceFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(BUTTERFLY, 0, 0, 0, 0));
    }

    @Test
    void testMoveWalkingInPlaceIntrudingFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(TURTLE, 0, 0, 0, 0));
    }

    @Test
    void testMoveWalkingOnTopIntrudingMiddle() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
    }

    @Test
    void testMoveWalkingOnFullIntrudingMiddle() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
    }

    @Test
    void testMoveFlyingInPlaceFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 0, 0));
    }

    @Test
    void testMoveFlyingInPlaceIntrudingFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(DOVE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(DOVE, 0, 0, 0, 0));
    }

    @Test
    void testMoveFlyingClose() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 2, 0));
    }

    @Test
    void testMoveFlyingFar() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_TOO_FAR, this.game1.moveCreature(HUMMINGBIRD, -1, 0, 3, 0));
    }

    @Test
    void testMoveFlyingNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 0));
    }

    @Test
    void testMoveFlyingOverFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(HUMMINGBIRD, -1, 0, 2, 0));
    }

    @Test
    void testMoveFlyingLandOnCreatureNoIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING, this.game1.moveCreature(HUMMINGBIRD, 0, 0, 0, 1));
    }

    @Test
    void testMoveFlyingLandOnCreatureIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(DOVE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(DOVE, 0, 0, 0, 1));
    }

    @Test
    void testMoveFlyingLandOnFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(DOVE, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_ERROR_FULL_TILE, this.game1.moveCreature(DOVE, 2, 0, 1, 0));
    }

    @Test
    void testMoveFlyingDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 1));
    }

    @Test
    void testMoveFlyingNotDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 1));
    }

    @Test
    void testMoveFlyingSurrounded() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HUMMINGBIRD, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_ERROR_FLYING_SURROUNDED, this.game1.moveCreature(HUMMINGBIRD, 0, 0, -1, 2));
    }

    @Test
    void testMoveRunningInPlaceFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(SPIDER, 0, 0, 0, 0));
    }

    @Test
    void testMoveRunningIntrudingFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(SPIDER, 0, 0, 0, 0));
    }

    @Test
    void testMoveRunningClose() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(HORSE, 0, 0, 1, -1));
    }

    @Test
    void testMoveRunningFar() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(HORSE, -1, 0, 3, -1));
    }

    @Test
    void testMoveRunningExact() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(HORSE, -1, 0, 1, 1));
    }

    @Test
    void testMoveRunningCloseIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(SPIDER, 0, 0, 1, -1));
    }

    @Test
    void testMoveRunningFarIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH, this.game1.moveCreature(SPIDER, -1, 0, 3, -1));
    }

    @Test
    void testMoveRunningFarNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(HORSE, -1, 0, -4, 0));
    }

    @Test
    void testMoveRunningExactIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 3, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningExactIntrudingOccupied() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningExactMiddleNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 3, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, 0, -2));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 4, -2));

        assertEquals(MoveResponse.MOVE_ERROR_NO_LEGAL_PATH, this.game1.moveCreature(HORSE, 0, -2, 3, -2));
    }

    @Test
    void testMoveRunningExactOnFullIntrudingMiddle() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 2, -1, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(SPIDER, -2, 0, 3, 0));
    }

    @Test
    void testMoveRunningIntrudingLandOnFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(SPIDER, -3, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 3, 0, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_FULL_TILE, this.game1.moveCreature(SPIDER, -3, 0, 2, 0));
    }

    @Test
    void testMoveRunningKamikazeLandOnFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(SPIDER, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(SPIDER, 2, 0, 1, 0));
    }

    @Test
    void testMoveRunningLandOnOccupied() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(HORSE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING, this.game1.moveCreature(HORSE, -1, 0, 2, 0));
    }

    @Test
    void testMoveJumpingInPlaceFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(GRASSHOPPER, 0, 0, 0, 0));
    }

    @Test
    void testMoveJumpingInPlaceIntrudingFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(RABBIT, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_SAME_TILE, this.game1.moveCreature(RABBIT, 0, 0, 0, 0));
    }

    @Test
    void testMoveJumpingCloseStraight() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(GRASSHOPPER, 0, 0, 2, 0));
    }

    @Test
    void testMoveJumpingCloseNotStraight() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_LINEAR, this.game1.moveCreature(GRASSHOPPER, 0, 0, 2, -1));
    }

    @Test
    void testMoveJumpingFar() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_TOO_FAR, this.game1.moveCreature(GRASSHOPPER, -1, 0, 3, 0));
    }

    @Test
    void testMoveJumpingNotConnected() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 0));
    }

    @Test
    void testMoveJumpingOverFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, 2, -1, 1,0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(GRASSHOPPER, -1, 0, 2, 0));
    }

    @Test
    void testMoveJumpingLandOnCreatureNoIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING, this.game1.moveCreature(GRASSHOPPER, 0, 0, 0, 1));
    }

    @Test
    void testMoveJumpingLandOnCreatureIntruding() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(RABBIT, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(RABBIT, 0, 0, 0, 1));
    }

    @Test
    void testMoveJumpingDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 1));
    }

    @Test
    void testMoveJumpingNotDraggable() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(GRASSHOPPER, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(GRASSHOPPER, 0, 0, -1, 1));
    }

    @Test
    void testMoveJumpingLandOnFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(RABBIT, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_ERROR_FULL_TILE, this.game1.moveCreature(RABBIT, 2, 0, 1, 0));
    }

    @Test
    void testMoveJumpingKamikazeLandOnFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(RABBIT, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(RABBIT, 2, 0, 1, 0));
    }

    @Test
    void testOrderStaysMoveSearchBottom() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(CRAB, 2, 0, 4, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));
    }

    @Test
    void testOrderStaysMoveSearchTop() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(TURTLE, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(TURTLE, -1, 0, 2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 3, -2));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_CONNECTED, this.game1.moveCreature(TURTLE, 2, 0, 4, 0));

        assertEquals(CRAB, this.game1.getCreatureAt(2, 0));
    }

    @Test
    void testPlaceCreatureNotInInventory() {
        assertEquals(MoveResponse.MOVE_ERROR_NOT_IN_INVENTORY, this.game1.placeCreature(DUCK, 0, 0));
    }

    @Test
    void testPlaceTooManyCreatures() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_ENOUGH_CREATURE,
                this.game1.placeCreature(BUTTERFLY, 0, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -3, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 4, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -4, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 5, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -5, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 6, 0));

        assertEquals(MoveResponse.MOVE_ERROR_NOT_ENOUGH_CREATURE,
                this.game1.placeCreature(CRAB, -6, 0));
    }

    @Test
    void testKamikazeEmpty() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(TURTLE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(TURTLE, 0, 0, 1, -1));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, -1));
    }

    @Test
    void testKamikazeOccupied() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(TURTLE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(TURTLE, 0, 0, 1, 0));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, 0));
    }

    @Test
    void testKamikazeFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(TURTLE, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(TURTLE, 2, 0, 1, 0));

        assertEquals(TURTLE, this.game2.getCreatureAt(1, 0, 1));
        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0, 0));
    }

    @Test
    void testKamikazeUpdateInventory() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(TURTLE, -2, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, 3, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(TURTLE, -2, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 3, -1));
    }

    @Test
    void testSwappingEmpty() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(DOVE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(DOVE, 0, 0, 1, -1));

        assertEquals(DOVE, this.game2.getCreatureAt(1, -1));
    }

    @Test
    void testSwappingButterflyFail() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(DOVE, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(DOVE, 0, 0, 1, 0));

        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0));
    }

    @Test
    void testSwappingFull() {
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.placeCreature(DOVE, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(CRAB, -1, 0, 1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game2.moveCreature(DOVE, 2, 0, 1, 0));

        assertEquals(DOVE, this.game2.getCreatureAt(1, 0, 1));
        assertEquals(BUTTERFLY, this.game2.getCreatureAt(1, 0, 0));
        assertEquals(CRAB, this.game2.getCreatureAt(2, 0, 0));
    }

    @Test
    void testPlaceBlueWin() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, -1, 1, 0, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 0, -1, 1, -1));

        assertEquals(MoveResponse.GAME_END_BLUE_WON,
                this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_ERROR_GAME_OVER, this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testPlaceRedWin() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, -2));
        assertEquals(MoveResponse.GAME_END_RED_WON,
                this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_GAME_OVER, this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testMoveBlueWin() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, -1, 1, 0, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.GAME_END_BLUE_WON,
                this.game1.moveCreature(CRAB, 0, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_GAME_OVER,
                this.game1.moveCreature(CRAB, 0, 0, 0, 0));
    }

    @Test
    void testMoveRedWin() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, 1, 1, 0, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, -2));
        assertEquals(MoveResponse.GAME_END_RED_WON,
                this.game1.moveCreature(CRAB, 2, -1, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_GAME_OVER, this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void testDraw() {
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 0, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(BUTTERFLY, 1, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 1, 1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 0, -1));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -1));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 0));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, 0));

        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, -1, 2));
        assertEquals(MoveResponse.MOVE_OK, this.game1.placeCreature(CRAB, 2, -2));

        assertEquals(MoveResponse.MOVE_OK, this.game1.moveCreature(CRAB, -1, 2, 0, 1));

        assertEquals(MoveResponse.GAME_END_DRAW,
                this.game1.moveCreature(CRAB, 2, -2, 1, -1));

        assertEquals(MoveResponse.MOVE_ERROR_GAME_OVER, this.game1.placeCreature(CRAB, -1, -1));
    }

    @Test
    void creatureNameToString() {
        assertEquals("Butterfly", BUTTERFLY.toString());
        assertEquals("Crab", CRAB.toString());
        assertEquals("Dove", DOVE.toString());
        assertEquals("Grasshopper", GRASSHOPPER.toString());
        assertEquals("Horse", HORSE.toString());
        assertEquals("Hummingbird", HUMMINGBIRD.toString());
        assertEquals("Rabbit", RABBIT.toString());
        assertEquals("Spider", SPIDER.toString());
        assertEquals("Turtle", TURTLE.toString());
    }

    @Test
    void creaturePropertyToString() {
        assertEquals("FLYING", CreatureProperty.FLYING.toString().toUpperCase());
        assertEquals("JUMPING", CreatureProperty.JUMPING.toString().toUpperCase());
        assertEquals("RUNNING", CreatureProperty.RUNNING.toString().toUpperCase());
    }

    @Test
    void moveResultConstructor() {
        MoveResponse moveResponse = new MoveResponse(OK);
        assertEquals(OK, moveResponse.moveResult());
        assertNull(moveResponse.message());
    }
}
