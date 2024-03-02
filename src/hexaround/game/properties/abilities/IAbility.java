package hexaround.game.properties.abilities;

import hexaround.game.board.Board;
import hexaround.game.entities.creature.CreatureName;

import java.util.Map;

public interface IAbility {
    void takeEffect(Board board, Map<Boolean, Map<CreatureName, Integer>> playerInventories,
                    CreatureName creature, boolean team, int fromX, int fromY, int toX, int toY, int index);
}
