package aood.battleship.exceptions;

import aood.battleship.Boat;

public class BoatOverlapException extends Exception {
    public BoatOverlapException(String message) {
        super(message);
    }

    public BoatOverlapException(Boat boat) {
      super("Could not place boat " + boat + " due to overlap.");
    }

}
