package hexaround.game.entities.creature;
import java.util.*;

public record Creature(CreatureName name, int maxDistance, Collection<CreatureProperty> properties) {
}
