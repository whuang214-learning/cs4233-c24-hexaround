/*
 * Copyright (c) 2023. Gary F. Pollice
 *
 * This files was developed for personal or educational purposes. All rights reserved.
 *
 *  You may use this software for any purpose except as follows:
 *  1) You may not submit this file without modification for any educational assignment
 *      unless it was provided to you as part of starting code that does not require modification.
 *  2) You may not remove this copyright, even if you have modified this file.
 */

package hexaround.game.board;

import hexaround.game.board.coordinate.Coordinate;
import hexaround.game.board.tile.CreatureTile;
import hexaround.game.entities.creature.CreatureName;

import java.util.*;

import static hexaround.game.board.coordinate.Coordinate.makeCoordinate;

/**
 * This class manages the game board. It provides all information about the board,
 * the state of the board, and other things that the board knows about.
 * The board is assumed to be infinite.
 */
public class Board {
    private Map<Coordinate, LinkedList<CreatureTile>> hexes = null;
    private Coordinate blueButterflyCoord = null;
    private Coordinate redButterflyCord = null;

    public Board() {
        this.hexes = new HashMap<>();
    }

    /**
     * Retrieves the creature at a specific position in the list of creatures at a given coordinate.
     *
     * @param coord The coordinate of the tile.
     * @param index The position in the list of creatures at the tile.
     * @return The creature at the specified position in the list of creatures at the tile, or null if the index is out of bounds.
     */
    public CreatureName getCreatureAt(Coordinate coord, int index) {
        LinkedList<CreatureTile> creatures = this.hexes.getOrDefault(coord, new LinkedList<>());

        if(index >= creatures.size()) {
            return null;
        }

        return creatures.get(index).creature();
    }

    /**
     * Get the butterfly tile for the given team.
     * @param team The team whose butterfly should be retrieved.
     * @return The coordinate of the butterfly tile for the given team.
     */
    public Coordinate getButterflyTile(boolean team) {
        return team ? this.blueButterflyCoord : this.redButterflyCord;
    }


    /**
     * Clears the butterfly tile for the given team.
     *
     * @param team If true, the blue butterfly tile is cleared. If false, the red butterfly tile is cleared.
     */
    public void removeButterfly(boolean team) {
        if (team) {
            this.blueButterflyCoord = null;
        } else {
            this.redButterflyCord = null;
        }
    }

    /**
     * Retrieves all creatures at a given coordinate.
     *
     * @param coord The coordinate of the tile.
     * @return A list of creatures at the specified coordinate. If no creatures are present, an empty list is returned.
     */
    public LinkedList<CreatureTile> getCreaturesAt(Coordinate coord) {
        return new LinkedList<>(this.hexes.getOrDefault(coord, new LinkedList<>()));
    }


    /**
     * Checks if a given tile is occupied by at least one creature.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return True if the tile is occupied, false otherwise.
     */
    public boolean isOccupied(int x, int y) {
        Coordinate coord = makeCoordinate(x, y);

        return this.hexes.containsKey(coord) && !this.hexes.get(coord).isEmpty();
    }

    /**
     * Checks if a given tile is fully occupied by two creatures.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return True if the tile is fully occupied, false otherwise.
     */
    public boolean isOccupiedByTwo(int x, int y) {
        return this.hexes.getOrDefault(makeCoordinate(x, y), new LinkedList<>()).size() == 2;
    }

    /**
     * Determines if a given tile has at least one occupied neighboring tile.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return True if there is at least one occupied neighboring tile, false otherwise.
     */
    public boolean hasNeighbors(int x, int y) {
        return makeCoordinate(x, y).returnAdjacentCoordinates().stream()
                .anyMatch(neighbor -> this.isOccupied(neighbor.x(), neighbor.y()));
    }

    /**
     * Determines if any neighboring tiles of a given tile contain a creature from a specified team.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param team The team to check for. If true, checks for the presence of a blue team creature. If false, checks for a red team creature.
     * @return True if at least one neighboring tile contains a creature from the specified team, false otherwise.
     */
    public boolean isAdjCoordSameTeam(int x, int y, boolean team) {
        return makeCoordinate(x, y).returnAdjacentCoordinates().stream()
                .filter(this.hexes::containsKey)
                .flatMap(neighbor -> this.hexes.get(neighbor).stream())
                .anyMatch(piece -> piece.team() == team);
    }

    /**
     * Determines if a given tile is connected to the rest of the colony.
     *
     * @return True if the tile is connected to the rest of the colony, false otherwise.
     */
    public boolean isConnected() {
        Coordinate firstHex = this.hexes.keySet().iterator().next();
        return this.getConnectedCreatures(firstHex, new HashSet<>()) == this.hexes.size();
    }

    // returns the size of the cluster that contains the given hex
    private int getConnectedCreatures(Coordinate coord, HashSet<Coordinate> seen) {
        if (!this.hexes.containsKey(coord) || seen.contains(coord)) {
            return 0;
        }
        seen.add(coord);

        return 1 + coord.returnAdjacentCoordinates().stream()
                .mapToInt(neighbor -> getConnectedCreatures(neighbor, seen))
                .sum();
    }

    /**
     * Places a creature at the specified coordinates on the board.
     *
     * @param creature The type of creature to be placed.
     * @param team The team the creature belongs to. (true for blue, false for red)
     * @param x The x coordinate where the creature will be placed.
     * @param y The y coordinate where the creature will be placed.
     */
    public void placeCreatureAt(CreatureName creature, boolean team, int x, int y) {
        Coordinate hex = makeCoordinate(x, y);
        int index = this.hexes.containsKey(hex) ? this.hexes.get(hex).size() : 0;
        this.placeCreatureAt(creature, team, x, y, index);
    }


    /**
     * Places a creature at the specified coordinates on the board at specified index.
     *
     * @param creature The type of creature to be placed.
     * @param team The team the creature belongs to. (true for blue, false for red)
     * @param x The x coordinate where the creature will be placed.
     * @param y The y coordinate where the creature will be placed.
     * @param index The position in the list of creatures at the tile where the creature will be placed.
     */
    public void placeCreatureAt(CreatureName creature, boolean team, int x, int y, int index) {
        Coordinate hex = makeCoordinate(x, y);
        LinkedList<CreatureTile> pieces = this.hexes.computeIfAbsent(hex, k -> new LinkedList<>());

        if(index >= 0 && index <= pieces.size()) {
            pieces.add(index, new CreatureTile(creature, team));
        } else {
            pieces.add(new CreatureTile(creature, team));
        }

        if(creature.equals(CreatureName.BUTTERFLY)) {
            if (team) {
                this.blueButterflyCoord = hex;
            } else {
                this.redButterflyCord = hex;
            }
        }
    }

    /**
     * Removes a creature from a specified coordinate on the board.
     *
     * @param creature The type of creature to be removed.
     * @param team The team the creature belongs to. If true, the creature belongs to the blue team. If false, the creature belongs to the red team.
     * @param x The x coordinate from where the creature will be removed.
     * @param y The y coordinate from where the creature will be removed.
     * @return The position (index) of the creature at its hex after removal. If the creature was the only one at the hex, the method returns 0.
     */
    public int removeCreature(CreatureName creature, boolean team, int x, int y) {
        Coordinate hex = makeCoordinate(x, y);
        LinkedList<CreatureTile> pieces = this.hexes.get(hex);

        if(pieces.size() == 1) {
            this.hexes.remove(hex);
            return 0;
        }

        CreatureTile creatureToRemove = new CreatureTile(creature, team);
        pieces.remove(creatureToRemove);

        return pieces.indexOf(creatureToRemove);
    }


    /**
     * Determines if a given tile is surrounded by occupied tiles.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return True if all neighboring tiles are occupied, false otherwise.
     */
    public boolean isSurrounded(int x, int y) {
        return makeCoordinate(x, y).returnAdjacentCoordinates().stream()
                .allMatch(neighbor -> this.isOccupied(neighbor.x(), neighbor.y()));
    }
}
