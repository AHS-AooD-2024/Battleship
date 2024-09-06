package aood.battleship;

import java.util.function.*;
import java.util.*;

/**
 * Test
 * 
 */
public class Test {

    private static List<Error> fails;

    public static void main(String[] args) {
        fails = new ArrayList<>();
        Test.positionTester();
        tryThrow();
    }

    public static void positionTester() {
        Position intIntPos = new Position(0, 0);
        System.out.println(intIntPos.getRow());
        System.out.println(intIntPos.getCol());
        System.out.println(intIntPos.getRowIndex());
        System.out.println(intIntPos.getColIndex());
        System.out.println(intIntPos.toString());

        Position charIntPos = new Position('B', 2);
        System.out.println(charIntPos.getRow());
        System.out.println(charIntPos.getCol());
        System.out.println(charIntPos.getRowIndex());
        System.out.println(charIntPos.getColIndex());
        System.out.println(charIntPos.toString());

        Position defaultConstructorPos = new Position();
        System.out.println(defaultConstructorPos.getRow());
        System.out.println(defaultConstructorPos.getCol());
        System.out.println(defaultConstructorPos.getRowIndex());
        System.out.println(defaultConstructorPos.getColIndex());
        System.out.println(defaultConstructorPos.toString());

        Position inputPosition = Position.getFromConsole("Enter a position: ");
        System.out.println(inputPosition.getRow());
        System.out.println(inputPosition.getCol());
        System.out.println(inputPosition.getRowIndex());
        System.out.println(inputPosition.getColIndex());
        System.out.println(inputPosition.toString());
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