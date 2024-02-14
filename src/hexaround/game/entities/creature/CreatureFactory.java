package hexaround.game.entities.creature;

import hexaround.config.CreatureDefinition;

import java.util.*;

public class CreatureFactory {
    private final Map<CreatureName, CreatureDefinition> creatureDefinitions = new HashMap<>();

/**
 * The constructor for the CreatureFactory class.
 * It initializes the creatureDefinitions map which will store the CreatureName as the key and the CreatureDefinition as the value.
 * @param definitions A collection of CreatureDefinition objects to be added to the creatureDefinitions map.
 */
public CreatureFactory(Collection<CreatureDefinition> definitions) {
    for (CreatureDefinition def : definitions) {
        creatureDefinitions.put(def.name(), def);
    }
}

/**
 * This method creates a new Creature object using the provided CreatureName.
 * It retrieves the CreatureDefinition associated with the CreatureName from the creatureDefinitions map,
 * and uses the properties of the CreatureDefinition to initialize the new Creature.
 * @param name The name of the creature to be created.
 * @return A new Creature object with the provided name, maximum distance, and properties.
 */
public Creature createCreature(CreatureName name) {
    CreatureDefinition def = creatureDefinitions.get(name);
    return new Creature(name, def.maxDistance(), def.properties());
}

}
