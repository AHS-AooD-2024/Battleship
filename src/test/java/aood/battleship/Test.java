package aood.battleship;

import java.util.function.*;
import java.util.*;

/**
 * Test
 */
public class Test {

    private static List<Error> fails;

    public static void main(String[] args) {
        fails = new ArrayList<>();
        Test.help();
        tryThrow();
    }

    public static void help() {
        Position pos = new Position(1, 1);
        Position pos2 = new Position('B', 2);
        
        assertTrue(pos.equals(pos2));

        Boat a = new Boat(Boat.Type.AircraftCarrier, pos, Boat.Orientation.Horizontal);
        Boat b = new Boat(Boat.Type.Battleship, pos, Boat.Orientation.Horizontal);
        Boat c = new Boat(Boat.Type.Cruiser, pos, Boat.Orientation.Horizontal);
        Boat d = new Boat(Boat.Type.Destroyer, pos, Boat.Orientation.Horizontal);
        Boat s = new Boat(Boat.Type.Submarine, pos, Boat.Orientation.Vertical);
        System.out.println(a.getAbbreviation());
    }

    public static void assertTrue(boolean cond) {
        if(!cond){
            fail("Boolean condition failed.");
        }
    }

    public static void assertTrue(BooleanSupplier cond) {
        if(!cond.getAsBoolean()){
            fail("Boolean condition failed.");
        }
    }

    private static void fail(String message) {
        fails.add(new AssertionError(message));
    }

    private static void tryThrow() {
        if(!fails.isEmpty()){
            throw new AssertionError("Tests failed\n" + fails);
        }
    }
}