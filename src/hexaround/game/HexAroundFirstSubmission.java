package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.game.board.Board;
import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.tile.CreatureTile;
import hexaround.game.entities.creature.CreatureName;
import hexaround.game.entities.creature.CreatureProperty;
import hexaround.game.entities.player.PlayerName;
import hexaround.game.board.move.MoveResponse;
import hexaround.game.properties.abilities.AbilityKamikaze;
import hexaround.game.properties.abilities.AbilitySwapping;
import hexaround.game.properties.abilities.IAbility;
import hexaround.game.properties.attributes.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hexaround.game.board.move.MoveResult.OK;

public class HexAroundFirstSubmission implements IHexAround1 {

    private Board board;
    private Map<CreatureName, CreatureDefinition> creatureDefinitions;
    private Map<CreatureProperty, IAttribute> attributes;
    private Map<CreatureProperty, IAbility> abilities;
    private Map<Boolean, Map<CreatureName, Integer>> playerInventories;
    private boolean team; // true = blue, false = red
    private int moves; // moves made by both players (2 moves = 1 turn)
    private boolean gameOver; // for preventing further moves after game over

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        this.initAttributesMap();
        this.initAbilitiesMap();
        this.team = true;
        this.moves = 0;
        this.gameOver = false;
    }

    /**
     * return the creature at the given position.
     * @param x the x coordinate of the tile.
     * @param y the y coordinate of the tile.
     * @return the creatureName at the given position.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return board.getCreatureAt(new Coordinate(x, y), 0);
    }

    /**
     * return the creature at the given position and index (if there are multiple creatures on the same tile).
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param index The index of the creature at the given position.
     * @return the creatureName at the given position and index.
     */
    public CreatureName getCreatureAt(int x, int y, int index) {
        return board.getCreatureAt(new Coordinate(x, y), index);
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property, false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        CreatureName creature = board.getCreatureAt(new Coordinate(x, y), 0);
        if (creature != null) {
            CreatureDefinition cd = creatureDefinitions.get(creature);
            if (cd != null) {
                return cd.properties().contains(property);
            }
        }
        return false;
    }

    /**
     * Determine if a creature has a given property.
     * @param creature A creature name.
     * @param property A creature property.
     * @return True if the creature has the property.
     */
    private boolean creatureHasProperty(CreatureName creature, CreatureProperty property) {
        return this.creatureDefinitions.get(creature).properties().contains(property);
    }

    /**
     * Given the x and y coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        return board.isOccupied(x, y);
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        CreatureName creature = board.getCreatureAt(new Coordinate(x1, y1), 0);
        if (creature == null) {
            return false;
        }
        CreatureDefinition cd = creatureDefinitions.get(creature);
        return Coordinate.makeCoordinate(x1, y1).distanceBetween(Coordinate.makeCoordinate(x2, y2)) <= cd.maxDistance();
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature A creature name.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        MoveResponse validationResponse = validatePlacement(creature, x, y);
        if (!validationResponse.moveResult().equals(OK)) {
            return validationResponse;
        }

        this.board.placeCreatureAt(creature, this.team, x, y);
        this.updateInventory(this.team, creature, -1);
        this.moves += 1;
        this.team = !this.team;

        MoveResponse gameOverResponse = this.getGameOverStatus();
        if(!gameOverResponse.moveResult().equals(OK)) {
            this.gameOver = true;
            return gameOverResponse;
        }

        return MoveResponse.MOVE_OK;
    }

    // validate the placement of the creature
    private MoveResponse validatePlacement(CreatureName creature, int x, int y) {
        if(this.gameOver) {
            return MoveResponse.MOVE_ERROR_GAME_OVER;
        }

        if(!this.playerHasCreature(this.team, creature)) {
            return MoveResponse.MOVE_ERROR_NOT_IN_INVENTORY;
        }

        if(!this.playerHasEnough(this.team, creature)) {
            return MoveResponse.MOVE_ERROR_NOT_ENOUGH_CREATURE;
        }

        if(this.isOccupied(x, y)) {
            return MoveResponse.MOVE_ERROR_OCCUPIED;
        }

        if(this.moves >= 1 && !this.board.hasNeighbors(x, y)) {
            return MoveResponse.MOVE_ERROR_NOT_CONNECTED;
        }

        if(this.moves >= 2 && this.board.isAdjCoordSameTeam(x, y, !this.team)) {
            return MoveResponse.MOVE_ERROR_ADJACENT_TO_OPPONENT;
        }

        if(this.moves >= 6 && !this.isButterflyPlaced(this.team) && !this.isCreatureButterfly(creature)) {
            return MoveResponse.MOVE_ERROR_BUTTERFLY_NOT_PLACED;
        }

        return MoveResponse.MOVE_OK;
    }

    /**
     * Moves a creature from one position to another on the board.
     *
     * @param creature The creature to be moved.
     * @param fromX The x coordinate of the current position of the creature.
     * @param fromY The y coordinate of the current position of the creature.
     * @param toX The x coordinate of the new position of the creature.
     * @param toY The y coordinate of the new position of the creature.
     * @return A MoveResponse object indicating the result of the move. If the move is valid, the method also updates the game state.
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        MoveResponse validationResponse = validateMove(creature, fromX, fromY, toX, toY);
        if (!validationResponse.moveResult().equals(OK)) {
            return validationResponse;
        }

        executeMove(creature, fromX, fromY, toX, toY);

        MoveResponse gameOverResponse = this.getGameOverStatus();
        if(!gameOverResponse.moveResult().equals(OK)) {
            this.gameOver = true;
            return gameOverResponse;
        }

        return MoveResponse.MOVE_OK;
    }

    // validate the move
    private MoveResponse validateMove(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        if(this.gameOver) {
            return MoveResponse.MOVE_ERROR_GAME_OVER;
        }

        if(!this.board.getCreaturesAt(new Coordinate(fromX, fromY)).contains(new CreatureTile(creature, this.team))) {
            return MoveResponse.MOVE_ERROR_NO_MATCHING_CREATURE;
        }

        List<CreatureProperty> attributes = this.getAllProperties(creature);

        MoveResponse legalMoveResponse = this.attributes.get(this.getAttribute(creature)).isLegalMove(this.board, creature,
                this.team, this.creatureHasProperty(creature, CreatureProperty.INTRUDING), attributes.size() > 0,
                fromX, fromY, toX, toY, this.creatureDefinitions.get(creature).maxDistance());

        if(!legalMoveResponse.moveResult().equals(OK)) {
            return legalMoveResponse;
        }

        return MoveResponse.MOVE_OK;
    }

    /**
     * Get the movement attribute of a given creature.
     * @param creature A creature name.
     * @return The CreatureProperty (movement ability) of this creature.
     */
    private CreatureProperty getAttribute(CreatureName creature) {
        return this.creatureDefinitions.get(creature).properties().stream()
                .filter(this.attributes::containsKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all properties of a given creature.
     * @param creature A creature name.
     * @return A list of all attributes for the given creature.
     */
    private List<CreatureProperty> getAllProperties(CreatureName creature) {
        return this.creatureDefinitions.get(creature).properties().stream()
                .filter(this.abilities::containsKey)
                .collect(Collectors.toList());
    }

    // execute the move
    private void executeMove(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        int index = this.board.removeCreature(creature, this.team, fromX, fromY);
        this.board.placeCreatureAt(creature, this.team, toX, toY);
        this.moves += 1;
        this.team = !this.team;

        List<CreatureProperty> attributes = this.getAllProperties(creature);
        for(CreatureProperty attribute : attributes) {
            this.abilities.get(attribute).takeEffect(board, this.playerInventories, creature, this.team,
                    fromX, fromY, toX, toY, index);
        }
    }

    // getters and setters

    public boolean getTeam() {
        return this.team;
    }

    public int getMoves() {
        return this.moves;
    }

    /************************************ Helpers *********************************/
    public void setBoard(Board board) {
        this.board = board;
    }

    public void makeCreatureDefinitions(Collection<CreatureDefinition> creatureDefs) {
        this.creatureDefinitions = new HashMap<>();
        for (CreatureDefinition cd : creatureDefs) {
            this.creatureDefinitions.put(cd.name(), cd);
        }

    }

    public void makePlayerInventories(Collection<PlayerConfiguration> playerConfigs) {
        this.playerInventories = new HashMap<>();

        for(PlayerConfiguration pc : playerConfigs) {
            boolean team = pc.Player().equals(PlayerName.BLUE);

            this.playerInventories.put(team, new HashMap<>(pc.creatures()));
        }
    }

    private void initAttributesMap() {
        this.attributes = new HashMap<>();
        this.attributes.put(CreatureProperty.WALKING, new AttributeWalking());
        this.attributes.put(CreatureProperty.RUNNING, new AttributeRunning());
        this.attributes.put(CreatureProperty.JUMPING, new AttributeJumping());
        this.attributes.put(CreatureProperty.FLYING, new AttributeFlying());
    }

    private void initAbilitiesMap() {
        this.abilities = new HashMap<>();
        this.abilities.put(CreatureProperty.KAMIKAZE, new AbilityKamikaze());
        this.abilities.put(CreatureProperty.SWAPPING, new AbilitySwapping());
    }


    // check if the butterfly is placed
    private boolean isButterflyPlaced(boolean team) {
        return this.board.getButterflyTile(team) != null;
    }

    // check if the creature is a butterfly
    private boolean isCreatureButterfly(CreatureName creature) {
        return creature.equals(CreatureName.BUTTERFLY);
    }

    // check if current player has the creature
    private boolean playerHasCreature(boolean team, CreatureName creature) {
        return this.playerInventories.get(team).containsKey(creature);
    }

    // check if player has enough of the creature
    private boolean playerHasEnough(boolean team, CreatureName creature) {
        return this.playerInventories.get(team).get(creature) > 0;
    }

    // update the inventory of the player by amount
    private void updateInventory(boolean team, CreatureName creature, int amount) {
        this.playerInventories.get(team).put(creature, this.playerInventories.get(team).get(creature) + amount);
    }

    // check if the butterfly is surrounded
    private boolean isButterflySurrounded(boolean team) {
        Coordinate butterfly = board.getButterflyTile(team);

        if(butterfly == null) {
            return false;
        }

        return this.board.isSurrounded(butterfly.x(), butterfly.y());
    }

    /**
     * Get the current status of the game.
     * @return The status of the game (blue win, red win, draw, continue game).
     */
    private MoveResponse getGameOverStatus() {
        boolean blueSurrounded = this.isButterflySurrounded(true);
        boolean redSurrounded = this.isButterflySurrounded(false);

        if(blueSurrounded && redSurrounded) {
            return MoveResponse.GAME_END_DRAW;
        }

        if(blueSurrounded) {
            return MoveResponse.GAME_END_RED_WON;
        }

        if(redSurrounded) {
            return MoveResponse.GAME_END_BLUE_WON;
        }

        return new MoveResponse(OK);
    }

}
