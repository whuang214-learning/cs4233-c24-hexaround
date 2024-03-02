package hexaround.game.properties.attributes;

import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.move.MoveResponse;
import hexaround.game.entities.creature.CreatureName;

import java.util.HashSet;

import static hexaround.game.board.coordinate.Coordinate.makeCoordinate;

public class AttributeRunning extends AbstractAttribute implements IAttribute {
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
        if(makeCoordinate(fromX, fromY).distanceBetween(makeCoordinate(toX, toY)) != distance) {
            return MoveResponse.MOVE_ERROR_DISTANCE_MISMATCH;
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

        return this.pathExists(board, creature, team, intruding, fromX, fromY, toX, toY, distance, new HashSet<>())
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
     * @param visited A HashSet storing the visited tiles for each path.
     * @return True if a path exists.
     */
    private boolean pathExists(Board board, CreatureName creature, boolean team, boolean intruding,
                               int x, int y, int toX, int toY, int remaining, HashSet<Coordinate> visited) {
        if(!board.isConnected()) {
            return false;
        }

        if(remaining == 0) {
            return x == toX && y == toY;
        }

        if(visited.contains(makeCoordinate(x, y))) {
            return false;
        }

        for(Coordinate neighbor : makeCoordinate(x, y).returnAdjacentCoordinates()) {
            int nextX = neighbor.x();
            int nextY = neighbor.y();

            if((!intruding && board.isOccupied(nextX, nextY)) ||
                    (!intruding && !canDrag(board, x, y, nextX, nextY))) {
                continue;
            }

            int index = board.removeCreature(creature, team, x, y);
            board.placeCreatureAt(creature, team, nextX, nextY);
            visited.add(makeCoordinate(x, y));

            boolean result = this.pathExists(board, creature, team, intruding, nextX, nextY, toX, toY,
                    remaining - 1, visited);

            board.removeCreature(creature, team, nextX, nextY);
            board.placeCreatureAt(creature, team, x, y, index);
            visited.remove(makeCoordinate(x, y));

            if(result) {
                return true;
            }
        }

        return false;
    }
}
