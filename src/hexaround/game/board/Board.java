package hexaround.game.board;
import hexaround.game.entities.creature.CreatureName;

import java.util.*;



public class Board {
    private Map<Coordinate, CreatureName> hexes;

/**
 * The constructor for the Board class.
 * It initializes the hexes map which will store the coordinates of the hexes and the names of the creatures occupying them.
 */
public Board() {
    hexes = new HashMap<>();
}

/**
 * This method allows a creature to be placed on the board at a specified coordinate.
 * If the coordinate is already occupied, it returns a MoveResponse with a MOVE_ERROR result and a message indicating that the hex is already occupied.
 * If the coordinate is not occupied, it places the creature at the coordinate and returns a MoveResponse with an OK result.
 * @param coordinate The coordinate at which the creature is to be placed.
 * @param creatureName The name of the creature to be placed.
 * @return A MoveResponse indicating the result of the move and an optional message.
 */
public MoveResponse placeCreature(Coordinate coordinate, CreatureName creatureName) {
    if (hexes.containsKey(coordinate)) {
        return new MoveResponse(MoveResult.MOVE_ERROR, "Hex is already occupied");
    }
    hexes.put(coordinate, creatureName);
    return new MoveResponse(MoveResult.OK);
}

/**
 * This method retrieves the name of the creature occupying a specified coordinate on the board.
 * If no creature is occupying the coordinate, it returns null.
 * @param coordinate The coordinate from which to retrieve the creature.
 * @return The name of the creature occupying the coordinate, or null if no creature is present.
 */
public CreatureName getCreatureAt(Coordinate coordinate) {
    return hexes.get(coordinate);
}

}
