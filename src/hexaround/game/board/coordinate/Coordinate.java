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

package hexaround.game.board.coordinate;


import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a Coordinate in the HexAround game. It
 * currently only has a default constructor that takes the two
 * axis values (x, y).
 */
public record Coordinate(int x, int y) {

    /**
     * Create a coordinate from the given x and y values.
     *
     * @param x
     * @param y
     * @return the specified coordinate
     */
    public static Coordinate makeCoordinate(int x, int y) {
        return new Coordinate(x, y);
    }

    /**
     * Gets the distance between two coordinates.
     * https://stackoverflow.com/questions/14491444/calculating-distance-on-a-hexagon-grid
     *
     * @param to the other coordinate
     * @return the distance between two coordinates
     */
    public int distanceBetween(Coordinate to) {
        return (Math.abs(x - to.x()) + Math.abs(x + y - to.x() - to.y()) + Math.abs(y - to.y())) / 2;
    }

    /**
     * check if the other coordinate is in a straight line
     * @param to The other coordinate.
     * @return true if the other coordinate is in a straight line
     */
    public boolean isLinear(Coordinate to) {
        return x == to.x() || y == to.y()
                || (x + y) == (to.x() + to.y());
    }


    /**
     * Get the coordinates of the tiles that are adjacent to this one.
     *
     * @return a collection of the adjacent coordinates
     */
    public Collection<Coordinate> returnAdjacentCoordinates() {
        return Stream.of(
                new Coordinate(x, y - 1),
                new Coordinate(x + 1, y - 1),
                new Coordinate(x + 1, y),
                new Coordinate(x, y + 1),
                new Coordinate(x - 1, y + 1),
                new Coordinate(x - 1, y)
        ).collect(Collectors.toList());
    }
}
