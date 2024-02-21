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
    return new MoveResponse(MoveResult.OK, "Legal move");
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

/**
 * This method moves a creature from one coordinate to another on the board.
 * If the destination coordinate is already occupied, it returns a MoveResponse with a MOVE_ERROR result and a message indicating that the hex is already occupied.
 * If the move would result in a disconnected colony, it returns a MoveResponse with a MOVE_ERROR result and a message indicating that the colony is not connected.
 * If the move is successful, it updates the board and returns a MoveResponse with an OK result.
 * @param from The coordinate from which the creature is to be moved.
 * @param to The coordinate to which the creature is to be moved.
 * @return A MoveResponse indicating the result of the move and an optional message.
 */
public MoveResponse moveCreature(Coordinate from, Coordinate to) {
    // check if there is no creature at the to coordinate
    // make a copy of the board
    // move the creature from the from coordinate to the to coordinate
    // run a DFS method to count all creatures that are connected to the to coordinate
    // if the count is less than the number of creatures on the board, return a MOVE_ERROR response

    if (getCreatureAt(to) != null) {
        return new MoveResponse(MoveResult.MOVE_ERROR, "Hex is already occupied");
    }
    Map<Coordinate, CreatureName> newHexBoard = new HashMap<>(hexes);
    CreatureName creatureToMove = newHexBoard.remove(from);
    newHexBoard.put(to, creatureToMove);

    // go through the hexes and count the number of creatures where value != null
    int originalHexesCount = 0;
    for (Map.Entry<Coordinate, CreatureName> entry : hexes.entrySet()) {
        if (entry.getValue() != null) {
            originalHexesCount++;
        }
    }

    if (countConnectedCreatures(to, newHexBoard) < originalHexesCount) {
        return new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again");
    }

    hexes = newHexBoard;
    return new MoveResponse(MoveResult.OK, "Legal move");
}

private int countConnectedCreatures(Coordinate coordinate, Map<Coordinate, CreatureName> hexes) {
    Set<Coordinate> visited = new HashSet<>();
    return dfs(coordinate, hexes, visited);
}

private int dfs(Coordinate coordinate, Map<Coordinate, CreatureName> hexes, Set<Coordinate> visited) {
    visited.add(coordinate);
    int count = 1;
    List<Coordinate> adjacentCoordinates = getAdjacentCoordinates(coordinate);
    for (Coordinate adjacentCoordinate : adjacentCoordinates) {
        if (hexes.get(adjacentCoordinate) != null && !visited.contains(adjacentCoordinate)) {
            count += dfs(adjacentCoordinate, hexes, visited);
        }
    }
    return count;
}

private List<Coordinate> getAdjacentCoordinates(Coordinate coordinate) {
    List<Coordinate> adjacentCoordinates = new ArrayList<>();
    int[][] directions = {{0, 1}, {1, 0}, {-1, 1}, {1, -1}, {0, -1}, {-1, 0}};

    for (int[] direction : directions) {
        Coordinate adjacentCoordinate = new Coordinate(coordinate.x() + direction[0], coordinate.y() + direction[1]);
        if (hexes.get(adjacentCoordinate) != null) {
            adjacentCoordinates.add(adjacentCoordinate);
        }
    }

    return adjacentCoordinates;
}
}
