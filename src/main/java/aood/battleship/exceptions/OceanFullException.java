package aood.battleship.exceptions;

public class OceanFullException extends IllegalStateException {
    public OceanFullException(String message) {
        super(message);
    }

    public OceanFullException() {
        super("Ocean full.");
    }
}
