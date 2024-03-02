package hexaround.game.properties.abilities;

import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.tile.CreatureTile;
import hexaround.game.entities.creature.CreatureName;

import java.util.List;
import java.util.Map;

public class AbilityKamikaze implements IAbility {
    /**
     * Force the piece under the recently moved piece back into its team's inventory.
     * @param board The hex board.
     * @param playerInventories Both team's creature inventories.
     * @param creature A creature name.
     * @param team A player team.
     * @param fromX The source x coordinate.
     * @param fromY The source y coordinate.
     * @param toX The destination x coordinate.
     * @param toY The destination y coordinate.
     * @param index The position of the piece at its old tile.
     */
    @Override
    public void takeEffect(Board board, Map<Boolean, Map<CreatureName, Integer>> playerInventories,
                           CreatureName creature, boolean team, int fromX, int fromY, int toX, int toY, int index) {
        List<CreatureTile> toCreatures = board.getCreaturesAt(new Coordinate(toX, toY));
        if(toCreatures.size() <= 1) {
            return;
        }

        CreatureTile under = toCreatures.get(toCreatures.size() - 2);
        CreatureName underCreatureName = under.creature();
        boolean underTeam = under.team();

        board.removeCreature(underCreatureName, underTeam, toX, toY);
        playerInventories.get(underTeam).merge(underCreatureName, 1, Integer::sum);

        if(underCreatureName == CreatureName.BUTTERFLY) {
            board.removeButterfly(underTeam);
        }
    }
}
