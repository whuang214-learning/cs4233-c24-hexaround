package hexaround.game.board;
import hexaround.game.entities.creature.CreatureName;

import java.util.*;



public class Board {
    private Map<Coordinate, CreatureName> hexes;

    public Board() {
        hexes = new HashMap<>();
    }

    public MoveResponse placeCreature(Coordinate coordinate, CreatureName creatureName) {
        if (hexes.containsKey(coordinate)) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "Hex is already occupied");
        }
        hexes.put(coordinate, creatureName);
        return new MoveResponse(MoveResult.OK);
    }

    public CreatureName getCreatureAt(Coordinate coordinate) {
        return hexes.get(coordinate);
    }

}
