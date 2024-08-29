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

        tryThrow();
    }

    public static void positions() {
        
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