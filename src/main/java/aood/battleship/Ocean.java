package aood.battleship;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import aood.battleship.Boat.Orientation;
import aood.battleship.exceptions.BoatOverlapException;
import aood.battleship.exceptions.OceanOutOfBoundsException;

/**
 * An Ocean for Battleship.
 * <p>
 * An Ocean holds the boats present for a player, and can be used to
 * retrieve and maintain data related to a game. Boats can be placed
 * and gotten by position.
 *
 * @see Boat
 */
public interface Ocean {
    /**
     * Places a boat in the ocean.
     *
     * @param boat The boat to place
     *
     * @throws BoatOverlapException If the placed boat would overlap with an
     * existing boat.
     */
    void place(Boat boat) throws BoatOverlapException;

    /**
     * Places a boat with the given parameters.
     *
     * @param type The type of boat to place.
     * @param orient The orientation of the boat to place.
     * @param pos The position to place the boat at.
     *
     * @throws BoatOverlapException If the placed boat would overlap with an
     * existing boat.
     * @throws OceanOutOfBoundsException If the given position would cause the
     * boat to extend beyond the bounds of the ocean.
     */
    default void place(Boat.Type type, Boat.Orientation orient, Position pos)
            throws BoatOverlapException, OceanOutOfBoundsException {
        place(new Boat(type, pos, orient));
    }

    
    /**
     * Places all five boats -- one of each type -- randomly in the ocean.
     */
    default void placeAllBoats() {
        Random rng = ThreadLocalRandom.current();

        for(Boat.Type type : Boat.Type.values()) {
            while(true) {
                Boat.Orientation orient = rng.nextBoolean() ? Orientation.Horizontal : Orientation.Vertical;
                
                final int furthestDown;
                final int furthestRight;

                // Avoid random positions that are doomed to fail.
                if(orient == Orientation.Horizontal) {
                    furthestDown = height();
                    furthestRight = width() - type.size();
                } else {
                    furthestDown = height() - type.size();
                    furthestRight = width();
                }

                Position pos = new Position(rng.nextInt(furthestDown), rng.nextInt(furthestRight));

                try {
                    place(type, orient, pos);
                    
                    // success
                    break;
                } catch (OceanOutOfBoundsException | BoatOverlapException e) {
                    // try again
                    continue;
                }
            }
        }
    }

    /**
     * Gets the width of this ocean, or the number of columns. Is 10 by default;
     * 
     * @return The number of columns.
     */
    default int width() {
        return 10;
    }

    /**
     * Gets the height of this ocean, or the number of rows. Is 10 by default;
     * @return
     */
    default int height() {
        return 10;
    }

    /**
     * Shoots at a position, recording potential hits.
     *
     * @param pos The position to shoot at.
     */
    void shoot(Position pos);

    /**
     * Checks if there is a boat that has been hit at the given position.
     *
     * @param pos The position to check at.
     * @return {@code true} if there is a boat hit on the given position,
     * {@code false} otherwise.
     */
    boolean isHit(Position pos);

    /**
     * Gets the initial of a boat that has been hit. If there is
     * no boat at the given position, or it has not been hit, returns
     * 0 ({@code '\0'}).
     *
     * @param pos The position to get from.
     * @return The applicable abbreviation of the present boat.
     */
    char getBoatInitial(Position pos);

    /**
     * Gets the boat present at a particular position, or {@code null} if
     * no boat is present.
     *
     * @param pos The position to get from.
     * @return The boat present at the position, or {@code null} if none is
     * present.
     */
    Boat get(Position pos);

    /**
     * Gets the type of boat present on a given position.
     *
     * @param pos The position to get from
     * @return The type of boat present.
     *
     * @see #get(Position)
     */
    Boat.Type getBoatType(Position pos);

    /**
     * Gets the name of the boat present on a given position.
     *
     * @param pos The position to get from
     * @return The name of the boat present.
     *
     * @see #get(Position)
     * @see #getBoatType(Position)
     */
    String getBoatName(Position pos);

    /**
     * Checks if there is a sunken boat present at the given position.
     *
     * @param pos The position to check from.
     * @return {@code true} if there is a sunken boat present, {@code false}
     * otherwise.
     *
     * @see #isAllSunk()
     * @see Boat#isSunk()
     */
    boolean isSunk(Position pos);

    /**
     * Checks if every boat present has been sunk.
     * @return {@code true} if every boat has been sunk, {@code false} otherwise.
     *
     * @see #isSunk(Position)
     * @see Boat#isSunk()
     */
    boolean isAllSunk();
}
