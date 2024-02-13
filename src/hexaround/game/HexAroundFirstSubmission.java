package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.game.board.*;
import hexaround.game.entities.creature.*;
import hexaround.game.entities.player.*;

import java.util.*;

public class HexAroundFirstSubmission implements IHexAround1 {


    // builder class
    public static class Builder {
        private HexAroundFirstSubmission gameManager ;

        public Builder() {
            gameManager  = new HexAroundFirstSubmission();
        }

        public Builder withCreatureDefinitions(Collection<CreatureDefinition> creatureDefinitions) {

            // instantiate CreatureFactory and pass creatureDefinitions
            CreatureFactory creatureFactory = new CreatureFactory(creatureDefinitions);

            // get all creatures and add them to the game manager
            creatureDefinitions.forEach(creatureDefinition -> {
                gameManager.addCreature(creatureFactory.createCreature(creatureDefinition.name()));
            });

            return this;
        }

        public Builder withPlayerConfigurations(Collection<PlayerConfiguration> playerConfigurations) {

            playerConfigurations.forEach(playerConfiguration -> {
                // update the players map with the player name and the list of creatures
                gameManager.addPlayer(playerConfiguration.Player(), playerConfiguration.creatures());
            });

            return this;
        }

        public HexAroundFirstSubmission build() {
            return gameManager;
        }
    }
    private final Board board;
    private List<Creature> allCreatures;
    private List<Player> players;

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    private HexAroundFirstSubmission() {
        // Nothing to do.
        board = new Board();
        allCreatures = new ArrayList<>();
        players = new ArrayList<>();
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x
     * @param y
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return board.getCreatureAt(new Coordinate(x, y));
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        CreatureName currentCreatureAtHex = board.getCreatureAt(new Coordinate(x, y));
        return allCreatures.stream()
                .filter(creature -> creature.name().equals(currentCreatureAtHex))
                .anyMatch(creature -> creature.properties().contains(property));
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x
     * @param y
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        return board.getCreatureAt(new Coordinate(x, y)) != null;
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return itrue if the distance between  the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        return false;
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature
     * @param x
     * @param y
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        return board.placeCreature(new Coordinate(x, y), creature);
    }

    /**
     * This is never used in this submission. You do not have to do anything.
     * @param creature
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        return null;
    }

    // add creature to the list
    private void addCreature(Creature creature) {
        allCreatures.add(creature);
    }

    // add player to the map
    private void addPlayer(PlayerName playerName, Map<CreatureName, Integer> creatures) {
        // add player record into the players list
        players.add(new Player(playerName, creatures));
    }

}
