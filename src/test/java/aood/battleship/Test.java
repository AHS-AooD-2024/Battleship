package aood.battleship;

import java.util.function.*;

import aood.battleship.Boat.Orientation;
import aood.battleship.Boat.Type;
import aood.battleship.exceptions.BoatOverlapException;

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
        Test.boatTester();
        Test.oceanTester();

        Test.gridTester();

        tryThrow();
    }

    public static void gridTester()
    {
//        BattleshipGrid grid = new BattleshipGridArray();
        BattleshipGrid grid = new CharArrayBattleshipGrid();
        Position topLeft = new Position(0, 0);
        Position middle = new Position(4, 4);
        grid.shoot(topLeft, true, 'A');

        //isHit method
        System.out.println("Expected true: " + grid.isHit(topLeft));
        assertTrue(grid.isHit(topLeft));

        //isMiss method
        System.out.println("Expected false: " + grid.isMiss(middle));
        assertTrue(!grid.isMiss(middle));

        //isEmpty method
        System.out.println("Expected true: " + grid.isEmpty(middle));
        assertTrue(grid.isEmpty(middle));

        grid.shoot(middle, false, 'D');

        //isMiss true case
        System.out.println("Expected true: " + grid.isMiss(middle));
        assertTrue(grid.isMiss(middle));

        //isHit false case
        System.out.println("Expected false: " + grid.isHit(middle));
        assertTrue(!grid.isHit(middle));

        //getBoatInitial method
        System.out.println("Expected 'A': " + grid.getBoatInitial(topLeft));
        assertTrue(grid.getBoatInitial(topLeft) == 'A');
        
        //getBoatInitial miss case
        System.out.println("Expected '0': " + grid.getBoatInitial(middle));
        assertTrue(!(grid.getBoatInitial(middle) == 'D'));
    }

    public static void oceanTester() {
        BoatArrayOcean bao = new BoatArrayOcean();


        try {
            bao.place(Type.AircraftCarrier, Orientation.Vertical, new Position('A', 4));
        } catch (BoatOverlapException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
        System.out.println(bao);
        System.out.println(bao.getGridView());
    }
    public static void boatTester() {
        //Boat Constructor
        Position topPosition = new Position(0, 5);
        Position bottomPosition = new Position(9, 5);
        Position leftPosition = new Position(5, 0);
        Position rightPosition = new Position(5, 9);
        Position midPosition = new Position(4, 4);

        Boat a = new Boat(Type.AircraftCarrier, topPosition, Orientation.Horizontal);
        Boat b = new Boat(Type.Battleship, bottomPosition, Orientation.Horizontal);
        Boat c = new Boat(Type.Cruiser, leftPosition, Orientation.Vertical);
        Boat d = new Boat(Type.Destroyer, rightPosition, Orientation.Vertical);
        Boat s = new Boat(Type.Submarine, midPosition, Orientation.Horizontal);

        //name method
        System.out.println(a.getName());
        
        //type method
        System.out.println(b.getType());

        //abbreviation method
        System.out.println(c.getAbbreviation());

        //size method
        System.out.println(d.size());

        //hits
        System.out.println(s.isHit(s.getPosition()));
        s.hit(s.getPosition());
        System.out.println(s.isHit(s.getPosition()));

        //sunk method
        System.out.println(d.isSunk());
        d.hit(d.getPosition());
        d.hit(new Position(6, 9));
        System.out.println(d.isSunk());

        //Horizontal + Vertical Orientation methods
        System.out.println(a.getDirection());
        System.out.println(a.isVertical());
        System.out.println(a.isHorizontal());
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