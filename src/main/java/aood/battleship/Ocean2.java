package aood.battleship;

import aood.battleship.Boat.Type;
import aood.battleship.exceptions.OceanOutOfBoundsException;
import aood.battleship.exceptions.BoatOverlapException;
import java.util.ArrayList;

/**
 * Ocean class
 * 
 * @author Nathan Li
 */
public class Ocean2 implements Ocean {

    private ArrayList<Boat> BoatArrayOcean;
    private final int ROWS = 10;
    private final int COLS = 10;

    public Ocean2()
    {
        BoatArrayOcean = new ArrayList<Boat>();
    }

    @Override
    public void place(Boat boat) throws BoatOverlapException, OceanOutOfBoundsException
    {
        for (Position pos : boat)
        {
            if(pos.getRowIndex() >= 0 && pos.getRowIndex() < ROWS && pos.getColIndex() >= 0 && pos.getColIndex() < COLS)
            {
                for (Boat placedBoat : BoatArrayOcean)
                {
                    if (placedBoat.onBoat(pos))
                        throw new BoatOverlapException(boat);
                }
            }
            else
                throw new OceanOutOfBoundsException("");
        }
        BoatArrayOcean.add(boat);
    }

    @Override
    public void shoot(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.onBoat(pos) && !placedBoat.isHit(pos))
            {
                placedBoat.hit(pos);
                break;
            }
        }
    }

    @Override
    public boolean isHit(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.isHit(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public char getBoatInitial(Position pos) {
        Boat temp = get(pos);
        if (temp != null)
            return temp.getAbbreviation();
        return '\0';
    }

    @Override
    public Boat get(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.onBoat(pos))
                return placedBoat;
        }

        return null;
    }

    @Override
    public Type getBoatType(Position pos) {
        Boat temp = get(pos);
        if (temp != null)
            return temp.getType();
        return null;
    }

    @Override
    public String getBoatName(Position pos) {
        Boat temp = get(pos);
        if (temp != null)
            return temp.getName();
        return null;
    }

    @Override
    public boolean isSunk(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.onBoat(pos) && placedBoat.isSunk())
                return true;
        }
        return false;
    }

    @Override
    public boolean isAllSunk() {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (!placedBoat.isSunk())
                return false;
        }
        return true;
    }

    
    @Override
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
}
