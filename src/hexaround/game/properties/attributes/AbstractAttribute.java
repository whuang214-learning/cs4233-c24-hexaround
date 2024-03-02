package hexaround.game.properties.attributes;

import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;

import java.util.HashSet;

import static hexaround.game.board.coordinate.Coordinate.makeCoordinate;

public abstract class AbstractAttribute {
    /**
     * Determine if a piece at (fromX, fromY) can be dragged to (toX, toY).
     * @param board The hex board.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @return True if the piece at (fromX, fromY) can be dragged to (toX, toY).
     */
    public boolean canDrag(Board board, int fromX, int fromY, int toX, int toY) {
        Coordinate from = makeCoordinate(fromX, fromY);
        Coordinate to = makeCoordinate(toX, toY);

        return from.returnAdjacentCoordinates().stream()
                .filter(hex -> !board.isOccupied(hex.x(), hex.y()))
                .anyMatch(to.returnAdjacentCoordinates()::contains);
    }
}
