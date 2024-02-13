package hexaround.game.entities.creature;

import hexaround.config.CreatureDefinition;

import java.util.*;

public class CreatureFactory {
    private final Map<CreatureName, CreatureDefinition> creatureDefinitions = new HashMap<>();

    public CreatureFactory(Collection<CreatureDefinition> definitions) {
        for (CreatureDefinition def : definitions) {
            creatureDefinitions.put(def.name(), def);
        }
    }

    public Creature createCreature(CreatureName name) {
        CreatureDefinition def = creatureDefinitions.get(name);
        if (def == null) {
            throw new IllegalArgumentException("No definition for creature: " + name);
        }
        return new Creature(name, def.maxDistance(), def.properties());
    }

}
