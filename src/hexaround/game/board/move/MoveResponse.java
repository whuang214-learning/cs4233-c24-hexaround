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

package hexaround.game.board.move;

/**
 * This is what is returned from making a move in a game.
 * @param moveResult
 * @param message
 */
public record MoveResponse(
    MoveResult moveResult,
    String message  // The message must be filled in if there is any error
){

    // static move response objects
    public static final MoveResponse MOVE_OK = new MoveResponse(MoveResult.OK, "Legal move.");
    public static final MoveResponse MOVE_ERROR_NO_LEGAL_PATH = new MoveResponse(MoveResult.MOVE_ERROR, "There is no legal path to the destination.");
    public static final MoveResponse MOVE_ERROR_GAME_OVER = new MoveResponse(MoveResult.MOVE_ERROR, "Cannot place a piece after the game is over.");
    public static final MoveResponse MOVE_ERROR_NOT_IN_INVENTORY = new MoveResponse(MoveResult.MOVE_ERROR, "Creature is not in the player's inventory.");
    public static final MoveResponse MOVE_ERROR_NOT_ENOUGH_CREATURE = new MoveResponse(MoveResult.MOVE_ERROR, "Player does not have enough of this creature.");
    public static final MoveResponse MOVE_ERROR_OCCUPIED = new MoveResponse(MoveResult.MOVE_ERROR, "Hex is already occupied.");
    public static final MoveResponse MOVE_ERROR_NOT_CONNECTED = new MoveResponse(MoveResult.MOVE_ERROR, "The colony must remain connected.");
    public static final MoveResponse MOVE_ERROR_ADJACENT_TO_OPPONENT = new MoveResponse(MoveResult.MOVE_ERROR, "Piece cannot be placed next to an opponent's piece.");
    public static final MoveResponse MOVE_ERROR_BUTTERFLY_NOT_PLACED = new MoveResponse(MoveResult.MOVE_ERROR, "Player must place their butterfly before placing other pieces.");
    public static final MoveResponse MOVE_ERROR_NO_MATCHING_CREATURE = new MoveResponse(MoveResult.MOVE_ERROR, "There is no matching creature piece to move on that tile.");
    public static final MoveResponse GAME_END_DRAW = new MoveResponse(MoveResult.DRAW, "The game is a draw. Both butterflies are surrounded.");
    public static final MoveResponse GAME_END_RED_WON = new MoveResponse(MoveResult.RED_WON, "Red wins! Blue's butterfly is surrounded.");
    public static final MoveResponse GAME_END_BLUE_WON = new MoveResponse(MoveResult.BLUE_WON, "Blue wins! Red's butterfly is surrounded.");
    public static final MoveResponse MOVE_ERROR_SAME_TILE = new MoveResponse(MoveResult.MOVE_ERROR, "Cannot travel back to the same tile.");
    public static final MoveResponse MOVE_ERROR_FULL_TILE = new MoveResponse(MoveResult.MOVE_ERROR, "The tile is already occupied with two pieces.");
    public static final MoveResponse MOVE_ERROR_TOO_FAR = new MoveResponse(MoveResult.MOVE_ERROR, "Cannot reach that tile with this piece.");
    public static final MoveResponse MOVE_ERROR_FLYING_SURROUNDED = new MoveResponse(MoveResult.MOVE_ERROR, "The flying piece is surrounded and cannot move.");
    public static final MoveResponse MOVE_ERROR_OCCUPIED_NOT_INTRUDING = new MoveResponse(MoveResult.MOVE_ERROR, "This piece is not intruding and the tile is occupied");
    public static final MoveResponse MOVE_ERROR_NOT_LINEAR = new MoveResponse(MoveResult.MOVE_ERROR, "The jumping piece must move in a straight line.");
    public static final MoveResponse MOVE_ERROR_DISTANCE_MISMATCH = new MoveResponse(MoveResult.MOVE_ERROR, "The distance between tiles must match the max distance for running pieces.");



    /**
     * Shortcut that calls the default constructor with a null message.
     * @param moveResult
     *
     */
    public MoveResponse(MoveResult moveResult) {
        this(moveResult, null);
    }
}
