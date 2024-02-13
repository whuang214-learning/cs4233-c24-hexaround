package hexaround.game.entities.player;

import hexaround.game.entities.creature.Creature;
import java.util.*;

public record Player(String name, Collection<Creature> creatures) {
}
