package aood.battleship;

import aood.battleship.exceptions.BoatOverlapException;
import aood.battleship.exceptions.OceanFullException;
import aood.battleship.exceptions.OceanOutOfBoundsException;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * An {@link Ocean} implementation for battleship.
 * <p>
 * This implementation tracks solely the boats themselves, and
 * will not use any space for any empty position in the ocean.
 * <p>
 * This implementation also provides the {@link #getGridView()}
 * method, which returns a grid style representation of the ocean
 * that is easier to comprehend. For example:
 * <pre>
 * + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10+
 * A   |   |   | A |   |   |   |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * B   |   |   | A |   |   |   |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * C   |   |   | A |   |   | C |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * D   |   |   | A |   |   | C |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * E   |   |   | A |   |   | C |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * F   |   |   |   |   |   |   |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * G   |   | B | B | B | B |   |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * H   |   |   |   |   |   |   | S |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * I   | D | D | D |   |   |   | S |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * J   |   |   |   |   |   |   |   |   |   |
 * + - + - + - + - + - + - + - + - + - + - +
 * </pre>
 *
 * @author Matthew Clark
 */
public class BoatArrayOcean implements Serializable, Iterable<Boat>, Ocean {
    private final Boat[] boats;
    
    private transient int q;
    
    private static final int ROWS = 10;
    private static final int COLS = 10;
    
    public BoatArrayOcean() {
        this(5);
    }
    
    public BoatArrayOcean(int numBoats) {
        boats = new Boat[numBoats];
        q = 0;
    }
    
    @Override
    public void place(Boat boat) throws BoatOverlapException {
        if(q >= boats.length)
            throw new OceanFullException();

        check(boat);

        boats[q++] = boat;
    }

    private void check(Boat boat) throws BoatOverlapException {
        // Valid boat check
        checkBoatParameters(boat);

        // Bounds
        Position pos = boat.getPosition();
        checkBounds(pos);
        checkExtension(pos, boat.getDirection(), boat.size());

        // Overlap
        for(Boat other : boats) {
            if(other == null) break; // End of boats reached

            // boat must be boat1 as the exceptions expect
            // the first boat to be the problem
            checkOverlap(boat, other);
        }
    }

    private static void checkBoatParameters(Boat boat) {
        Objects.requireNonNull(boat, "Cannot add null boat.");
        Objects.requireNonNull(boat.getType(), "Bad Type: boat type cannot be null.");
        Objects.requireNonNull(boat.getDirection(), "Bad Orientation: boat direction cannot be null.");
    }

    private static void checkBounds(Position pos) {
        final int col = pos.getColIndex();
        final int row = pos.getRowIndex();
        if(col < 0 || col >= COLS || row < 0 || row >= ROWS)
            throw new OceanOutOfBoundsException(pos);
    }

    private static void checkExtension(Position pos, Boat.Orientation orient, int size) {
        if (orient == Boat.Orientation.Horizontal) checkHorizontalExtension(pos, size);
        else checkVerticalExtension(pos, size);
    }
    
    private static void checkHorizontalExtension(Position pos, int size) {
        final int row = pos.getRowIndex() + size;
        if(row < 0 || row >= ROWS)
            badExtends(pos, Boat.Orientation.Horizontal, size);

    }

    private static void checkVerticalExtension(Position pos, int size) {
        final int col = pos.getColIndex() + size;
        if(col < 0 || col >= ROWS)
            badExtends(pos, Boat.Orientation.Vertical, size);

    }

    private static void badExtends(Position pos, Boat.Orientation orient, int size) {
        throw new OceanOutOfBoundsException("Position extends out of bounds: " + pos + " + " + size + " " + orient);
    }

    private static void checkOverlap(Boat boat1, Boat boat2) throws BoatOverlapException {
        if(boat1.getDirection() == boat2.getDirection()) {
          checkOverlapSameDirection(boat1, boat2);
        } else {
          checkOverlapDifferentDirections(boat1, boat2);
        }
    }

    private static void checkOverlapDifferentDirections(Boat boat1, Boat boat2) throws BoatOverlapException {
        // 0 0 1 0 0
        // 0 0 1 0 0
        // 0 0 1 0 0
        // 0 2 3 2 2
        // 0 0 1 0 0
        //
        // let y = y coordinate
        // let x = x coordinate
        // let s = size
        // let _v = vertical boat suffix
        // let _h = horizontal boat suffix
        //
        // let y_v + s_v - 1 = b_v; the y of the tail of the vertical boat
        // let x_h + s_h - 1 = b_h; the x of the tail of the horizontal boat
        //
        // to check the horizontal boat is within the vertical boat's y range:
        // y_v <= y_h <= b_v
        //
        // to check the vertical boat is within the horizontal boat's x domain:
        // x_h <= x_v <= b_h
        //
        // if both of these are true, then the boats must overlap
        Boat hBoat;
        Boat vBoat;

        // find which boat is which
        if(boat1.isHorizontal()){
            hBoat = boat1;
            vBoat = boat2;
        } else {
            hBoat = boat2;
            vBoat = boat1;
        }

        final Position hPos = hBoat.getPosition();
        final Position vPos = vBoat.getPosition();

        final int hBoatTailColumn = hPos.getColIndex() + hBoat.size() - 1;
        final int vBoatTailRow = vPos.getRowIndex() + vBoat.size() - 1;

        if(
                vPos.getRowIndex() <= hPos.getRowIndex() && hPos.getRowIndex() <= vBoatTailRow &&
                        hPos.getColIndex() <= vPos.getColIndex() && vPos.getColIndex() <= hBoatTailColumn
        ){
            throw new BoatOverlapException(boat1);
        }
    }

    private static void checkOverlapSameDirection(Boat boat1, Boat boat2) throws BoatOverlapException {
        // If the boats face the same direction, we only have to check that their
        // starting positions are not overlapping with the rest of the boat
        //
        // 1 1 1 1
        //       2 2 2
        //       ^ boat2.position is on boat1
        //       1 1 1
        // 2 2 2 2
        //       ^ boat1.position is on boat2
        // One of these cases will be true if two same facing boats overlap
        // of course, this is only true if they are on the same column/row

        // So assert that that is the case
        if(!boatsCanOverlapSameDirection(boat1, boat2)){
            return;
        }

        // Then check
        if(boat1.onBoat(boat2.getPosition()) || boat2.onBoat(boat1.getPosition())){
            throw new BoatOverlapException(boat1);
        }
    }

    private static boolean boatsCanOverlapSameDirection(Boat boat1, Boat boat2) {
        final Position pos1 = boat1.getPosition();
        final Position pos2 = boat2.getPosition();
        // Assume boats are same direction
        if (boat1.getDirection() == Boat.Orientation.Horizontal)
             return pos1.getRowIndex() == pos2.getRowIndex();
        else return pos1.getColIndex() == pos2.getColIndex();
    }

    @Override
    public void shoot(Position pos) {
        Boat boat = get(pos);

        // Missed, lol
        if(boat == null) return;

        boat.hit(pos);
    }

    @Override
    public boolean isHit(Position pos) {
        Boat boat = get(pos);

        return boat != null && boat.isHit(pos);
    }

    @Override
    public char getBoatInitial(Position pos) {
        Boat boat = get(pos);

        return boat == null ? 0 : boat.getAbbreviation();
    }

    @Override
    public Boat get(Position pos) {
        for(Boat boat : boats) {
            if(boat == null) break;
            if(boat.onBoat(pos)) {
                return boat;
            }
        }
        return null;
    }

    @Override
    public Boat.Type getBoatType(Position pos) {
        Boat boat = get(pos);

        return boat == null ? null : boat.getType();
    }

    @Override
    public String getBoatName(Position pos) {
        Boat boat = get(pos);

        return boat == null ? null : boat.getName();
    }

    @Override
    public boolean isSunk(Position pos) {
        Boat boat = get(pos);

        return boat != null && boat.isSunk();
    }

    @Override
    public boolean isAllSunk() {
        for(Boat boat : boats) {
            if(boat == null) break;
            if(!boat.isSunk())
                return false;
        }
        return true;
    }

    @Override
    public Iterator<Boat> iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public Spliterator<Boat> spliterator() {
        return Arrays.spliterator(boats, 0, q);
    }

    @Override
    public void forEach(Consumer<? super Boat> action) {
        for(int i = 0; i < q; i++){
            action.accept(boats[i]);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BoatArrayOcean other = (BoatArrayOcean) obj;
        return Objects.deepEquals(boats, other.boats);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(boats);
    }

    @Override
    public String toString() {
        return "BoatArrayOcean{" +
                "boats=" + Arrays.toString(boats) +
                '}';
    }

    public String getGridView() {
        StringBuilder sb = new StringBuilder("+ 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10+\n");
        for(int row = 0; row < ROWS; row++) {
            sb.append((char)(row + 'A'));
            for(int col = 0; col < COLS; col++) {
                Position here = new Position(row, col);

                char ch = getBoatInitial(here);
                // Replace null with space
                ch = ch != 0 ? ch : ' ';

                sb.append(' ').append(ch).append(" |");
            }
            sb.append("\n+ - + - + - + - + - + - + - + - + - + - +\n");
        }

        return sb.toString();
    }

    public void placeAllBoats()
    {
        for (Boat.Type boatType : Boat.Type.values())
        {
            try
            {
                placeRandomBoat(boatType);
            }
            catch (BoatOverlapException e)
            {
                System.out.println("overlap");
            }
        }
    }
    private void placeRandomBoat(Boat.Type boatType) throws BoatOverlapException
    {
        Boat.Orientation o = Boat.Orientation.values()[(int)(Math.random() * 2)];

        int row = (int)(Math.random() * ROWS);
        int col = (int)(Math.random() * COLS);

        if (o == Boat.Orientation.Horizontal)
            col = (int)(Math.random() * (COLS - boatType.size()));
        else    
            row -= (int)(Math.random() * (ROWS - boatType.size()));

        Position pos = new Position(row, col);

        place(new Boat(boatType, pos, o));
    }

    public Boat getBoat(int index)
    {
        return boats[index];
    }
}
