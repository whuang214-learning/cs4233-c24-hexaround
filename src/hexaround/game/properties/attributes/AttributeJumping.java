package hexaround.game.properties.attributes;

import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.move.MoveResponse;
import hexaround.game.entities.creature.CreatureName;

import static hexaround.game.board.coordinate.Coordinate.makeCoordinate;

public class AttributeJumping extends AbstractAttribute implements IAttribute {
    /**
     * Determine if the move from (fromX, fromY) to (toX, toY) is legal.
     *
     * @param board     The hex board.
     * @param creature  The name of the creature.
     * @param team      The team the creature is on.
     * @param intruding Whether the creature at (fromX, fromY) has the intruding attribute.
     * @param fromX     The source x coordinate.
     * @param fromY     The source y coordinate.
     * @param toX       The destination x coordinate.
     * @param toY       The destination y coordinate.
     * @param distance  The max distance this piece can move.
     * @return True if the move is legal.
     */
    @Override
    public MoveResponse isLegalMove(Board board, CreatureName creature, boolean team, boolean intruding,
                                    boolean removes, int fromX, int fromY, int toX, int toY, int distance) {
        Coordinate from = makeCoordinate(fromX, fromY);
        Coordinate to = makeCoordinate(toX, toY);

        if (from.equals(to)) {
            return MoveResponse.MOVE_ERROR_SAME_TILE;
        }

        if (board.isOccupiedByTwo(toX, toY) && !removes) {
            return MoveResponse.MOVE_ERROR_FULL_TILE;
        }

        if (board.isOccupied(toX, toY) && !intruding) {
            return MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING;
        }

        if (from.distanceBetween(to) > distance) {
            return MoveResponse.MOVE_ERROR_TOO_FAR;
        }

        if (!from.isLinear(to)) {
            return MoveResponse.MOVE_ERROR_NOT_LINEAR;
        }

        int index = board.removeCreature(creature, team, fromX, fromY);
        board.placeCreatureAt(creature, team, toX, toY);

        // if not connected after move, revert the move
        MoveResponse response = board.isConnected() ? MoveResponse.MOVE_OK : MoveResponse.MOVE_ERROR_NOT_CONNECTED;

        board.removeCreature(creature, team, toX, toY);
        board.placeCreatureAt(creature, team, fromX, fromY, index);

        return response;
    }
}
