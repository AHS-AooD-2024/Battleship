package aood.battleship.exceptions;

import aood.battleship.Position;

public class OceanOutOfBoundsException extends IndexOutOfBoundsException {
    public OceanOutOfBoundsException(String message) {
        super(message);
    }

    public OceanOutOfBoundsException(Position pos) {
        super("Position out of bounds: " + pos);
    }
}
