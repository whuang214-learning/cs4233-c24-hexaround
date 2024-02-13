package hexaround.game.entities.player;

import hexaround.game.entities.creature.CreatureName;

import java.util.*;

public record Player(PlayerName name, Map<CreatureName, Integer> creatures) {
}
