package hexaround.board;
import hexaround.required.CreatureName;

import java.util.*;


public class Board {
    private final Map<Coordinate, CreatureName> hexes = new HashMap<>();

    public Board() {}

    public void placeCreature(int x, int y, CreatureName creature) {
        hexes.put(new Coordinate(x, y), creature);
    }

}
