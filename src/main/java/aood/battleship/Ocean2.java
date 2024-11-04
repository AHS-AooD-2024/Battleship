package aood.battleship;

import aood.battleship.Boat.Orientation;
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
    private final int ROW = 10;
    private final int COL = 10;

    public Ocean2()
    {
        BoatArrayOcean = new ArrayList<Boat>();
    }

    @Override
    public void place(Boat boat) throws BoatOverlapException, OceanOutOfBoundsException
    {
        for (Position pos : boat)
        {
            if(pos.getRowIndex() >= 0 && pos.getRowIndex() < ROW && pos.getColIndex() >= 0 && pos.getColIndex() < COL)
            {
                for (Boat placedBoat : BoatArrayOcean)
                {
                    if (placedBoat.onBoat(pos))
                        throw new BoatOverlapException(boat);
                }
            }
            else
                throw new OceanOutOfBoundsException(pos);
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
    public void placeAllBoats() {
        Type[] types = Type.values();
        Orientation[] orients = Orientation.values();
        for (int i = 0; i < types.length; i++)
        {
            int orient = (int)(Math.random() * 2);

            int dRow = orient == 0 ? 0 : types[i].size();
            int dCol = orient == 1 ? 0 : types[i].size();

            try {
                place(new Boat(types[i], new Position((int)(Math.random() * (ROW - dRow)), (int)(Math.random() * (COL - dCol))), orients[orient]));
            } catch (BoatOverlapException e) {
                i--;
            }
        }
    }


    //Used for Testing and Debugging
    public Boat getBoat(int i)
    {
        return BoatArrayOcean.get(i);
    }
}
