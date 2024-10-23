package aood.battleship;

/**
 * An informational grid in the game of Battleship.
 * <p>
 * Each square has three states:
 * <ul>
 *     <li>EMPTY - has not been shot at</li>
 *     <li>MISS - has been shot and confirmed missed</li>
 *     <li>HIT - has been shot and confirmed hit</li>
 * </ul>
 *
 * This information is for the player, and should not itself be
 * able to affect any of the game state.
 */
public interface BattleshipGrid {
    /**
     * Records information to the grid.
     *
     * @param pos The position shot at.
     * @param isHit Whether the shot was a hit.
     * @param boatInitial The possible boat initial.
     * This value is ignored if {@code isHit} is {@code false}
     */
    void shoot(Position pos, boolean isHit, char boatInitial);

    /**
     * Checks whether a position was a hit.
     *
     * @param pos The position to check.
     * @return {@code true} if the position has recorded a hit,
     * {@code false} otherwise
     */
    boolean isHit(Position pos);

    /**
     * Checks whether a position was a miss.
     *
     * @param pos The position to check.
     * @return {@code true} if the position has recorded a miss,
     * {@code false} otherwise
     */
    boolean isMiss(Position pos);

    /**
     * Checks if a position has not been shot at yet.
     *
     * @param pos The position to check
     * @return {@code true} if the position has ***not*** been
     * shot at yet, {@code false} otherwise.
     */
    boolean isEmpty(Position pos);

    /**
     * Gets the character initial of the boat that has
     * been recorded to have been hit. Should return {@code '0'}
     * if the space was not hit.
     *
     * @param pos The position to get from.
     * @return The initial of the boat hit at the given position.
     */
    char getBoatInitial(Position pos);

    /**
     * Gets the number of columns in this grid. Defaults to 10.
     * 
     * @return The width of this grid.
     */
    default int width() {
        return 10;
    }

    /**
     * Gets the number of rows in this grid. Defaults to 10.
     * 
     * @return The height of this grid.
     */
    default int height() {
        return 10;
    }
}
