package aood.battleship;

import aood.battleship.exceptions.BoatOverlapException;
import aood.battleship.exceptions.OceanOutOfBoundsException;

public interface Ocean {
    /**
     * Places a boat in the ocean.
     *
     * @param boat The boat to place
     */
    void place(Boat boat) throws BoatOverlapException, OceanOutOfBoundsException;

    default void place(Boat.Type type, Boat.Orientation orient, Position pos)
            throws BoatOverlapException, OceanOutOfBoundsException {
        place(new Boat(type, pos, orient));
    }

    void shoot(Position pos);

    boolean isHit(Position pos);

    char getBoatInitial(Position pos);

    Boat get(Position pos);

    Boat.Type getBoatType(Position pos);

    String getBoatName(Position pos);

    boolean isSunk(Position pos);

    boolean isAllSunk(Position pos);
}
