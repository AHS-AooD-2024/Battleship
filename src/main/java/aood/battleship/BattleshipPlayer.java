package aood.battleship;

/**
 * A player that can play battleship.
 * <p>
 * All input and output for user-players will be handled by an implementation
 * of this interface. {@link #getShot()} can get input and 
 * {@link #updatePlayer(Position, boolean, char, Boat, boolean, boolean, boolean, int)}
 * can give output. For a slightly different style of implementation that uses an {@code on___()}
 * format, see {@link BasePlayer}.
 * 
 * @see BasePlayer
 */
public interface BattleshipPlayer {
    /**
     * Starts the game on the player's side. The player should not lock or
     * hold any resources from this method call; it is merely to indicate
     * to the player that the game has begun.
     */
    void startGame();

    /**
     * Gets the name of the player.
     * 
     * @return The name of the player.
     */
    String getName();

    /**
     * Gets the player's chosen shot. There is no gaurentee that multiple
     * calls of this method between calls to {@link #updatePlayer(Position, boolean, char, Boat, boolean, boolean, boolean, int)}
     * will return the same result.
     * 
     * @return The player's shot for the turn.
     */
    Position getShot();

    /**
     * Updates the player's grid information without considering a turn.
     * 
     * @param pos The position to update
     * @param didHit Whether the position was hit.
     * @param boatInitial Potential boat initial. Ignored if {@code didHit} is false.
     */
    void updateGrid(Position pos, boolean didHit, char boatInitial);

    /**
     * Gets the grid of information available to this player.
     * The return value should not be modified (see {@link #updateGrid(Position, boolean, char)}).
     * 
     * @return The player's information
     */
    BattleshipGrid getGrid();

    /**
     * Resets the player's available information to a fresh grid.
     */
    void resetGrid();

    /**
     * Updates the player to a given turn.
     * 
     * @param pos The position considered this turn.
     * @param isHit Whether the player's shot hit.
     * @param initial Potential boat initial; ignored if {@code isHit} is {@code false}.
     * @param boat The boat that was hit; {@code null} if {@code isHit} is {@code false}
     * @param isSunk Whether the hit boat was sunk.
     * @param gameOver Whether the game is over for any reason.
     * @param tooManyTurns {@code true} if the game has lated too long.
     * @param turns How many turns this game has lasted.
     */
    void updatePlayer(Position pos, boolean isHit, char initial, Boat boat, boolean isSunk, boolean gameOver, boolean tooManyTurns, int turns);
}
