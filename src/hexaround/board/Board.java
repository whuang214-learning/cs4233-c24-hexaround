package hexaround.board;
import hexaround.required.CreatureName;

import java.util.*;


public class Board {
    private Map<Coordinate, CreatureName> hexes;

    public Board() {
        hexes = new HashMap<>();
    }

    public void placeCreature(Coordinate coordinate, CreatureName creatureName) {
        hexes.put(coordinate, creatureName);
    }

    public CreatureName getCreatureAt(Coordinate coordinate) {
        return hexes.get(coordinate);
    }

}
