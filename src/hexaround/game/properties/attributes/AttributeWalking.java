package hexaround.game.properties.attributes;

import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.move.MoveResponse;
import hexaround.game.entities.creature.CreatureName;

import java.util.HashMap;

import static hexaround.game.board.coordinate.Coordinate.makeCoordinate;

public class AttributeWalking extends AbstractAttribute implements IAttribute {
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
    public MoveResponse isLegalMove(Board board, CreatureName creature, boolean team, boolean intruding,
                                    boolean removes, int fromX, int fromY, int toX, int toY, int distance) {
        if(fromX == toX && fromY == toY) {
            return MoveResponse.MOVE_ERROR_SAME_TILE;
        }

        if(!board.hasNeighbors(toX, toY)) {
            return MoveResponse.MOVE_ERROR_NOT_CONNECTED;
        }

        if(board.isOccupiedByTwo(toX, toY) && !removes) {
            return MoveResponse.MOVE_ERROR_FULL_TILE;
        }

        if(board.isOccupied(toX, toY) && !intruding) {
            return MoveResponse.MOVE_ERROR_OCCUPIED_NOT_INTRUDING;
        }

        if(makeCoordinate(fromX, fromY).distanceBetween(makeCoordinate(toX, toY)) > distance) {
            return MoveResponse.MOVE_ERROR_TOO_FAR;
        }
        return this.pathExists(board, creature, team, intruding, fromX, fromY, toX, toY, distance, new HashMap<>())
                ? MoveResponse.MOVE_OK
                : MoveResponse.MOVE_ERROR_NO_LEGAL_PATH;
    }

    /**
     * Recursively determine if there is a path from (x, y) to (toX, toY).
     * @param board The hex board.
     * @param creature A creature name.
     * @param team The creature's team.
     * @param intruding Whether the creature has the intruding attribute.
     * @param x The current x coordinate.
     * @param y The current y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @param remaining The number of remaining moves.
     * @param record A HashMap storing the highest number of moves remaining for each visited tile.
     * @return True if a path exists.
     */
    private boolean pathExists(Board board, CreatureName creature, boolean team, boolean intruding,
                               int x, int y, int toX, int toY, int remaining, HashMap<Coordinate, Integer> record) {
        if(!board.isConnected()) {
            return false;
        }

        if(x == toX && y == toY) {
            return true;
        }

        if(remaining == 0) {
            return false;
        }

        Coordinate hex = makeCoordinate(x, y);
        if(record.getOrDefault(hex, 0) >= remaining) {
            return false;
        }
        record.put(hex, remaining);

        for(Coordinate neighbor : hex.returnAdjacentCoordinates()) {
            int nextX = neighbor.x();
            int nextY = neighbor.y();

            if(!intruding && board.isOccupied(nextX, nextY)) {
                continue;
            }

            if(!intruding && !canDrag(board, x, y, nextX, nextY)) {
                continue;
            }

            int index = board.removeCreature(creature, team, x, y);
            board.placeCreatureAt(creature, team, nextX, nextY);

            boolean result = this.pathExists(board, creature, team, intruding, nextX, nextY, toX, toY,
                    remaining - 1, record);

            board.removeCreature(creature, team, nextX, nextY);
            board.placeCreatureAt(creature, team, x, y, index);

            if(result) {
                return true;
            }
        }

        return false;
    }
}
