package aood.battleship;

import aood.battleship.Boat.Type;
import aood.battleship.exceptions.OceanOutOfBoundsException;
import aood.battleship.exceptions.BoatOverlapException;
import java.util.ArrayList;

/**
 * Ocean class
 * 
 */
public class Ocean2 implements Ocean {

    private ArrayList<Boat> BoatArrayOcean;
    private final int HEIGHT = 10;
    private final int WIDTH = 10;

    public Ocean2()
    {
        BoatArrayOcean = new ArrayList<Boat>();
    }

    @Override
    public void place(Boat boat) throws BoatOverlapException, IndexOutOfBoundsException{
        // TODO Auto-generated method stub

        for (Position pos : boat)
        {
            if (pos.equals(PositionChecker(pos)))
            {
                for (Boat placedBoat : BoatArrayOcean)
                {
                    if (placedBoat.onBoat(pos))
                        throw new BoatOverlapException(boat);
                }
            }
            else
                throw new IndexOutOfBoundsException();
        }
        BoatArrayOcean.add(boat);
    }

    @Override
    public void shoot(Position pos) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.onBoat(pos) && !placedBoat.isHit(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public char getBoatInitial(Position pos) {
        // TODO Auto-generated method stub
        for (Boat placedBoat : BoatArrayOcean)
        {
           if (placedBoat.onBoat(pos))
                return placedBoat.getAbbreviation();
        }
        //assumed that position is hit
        //shouldn't reach here
        return '\0';
    }

    @Override
    public Boat get(Position pos) {
        // TODO Auto-generated method stub
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (placedBoat.onBoat(pos))
                return placedBoat;
        }

        return null;
    }

    @Override
    public Type getBoatType(Position pos) {
        // TODO Auto-generated method stub
        Boat temp = get(pos);
        if (temp != null)
            return temp.getType();
        return null;
    }

    @Override
    public String getBoatName(Position pos) {
        // TODO Auto-generated method stub

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
    public boolean isAllSunk(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
            if (!placedBoat.isSunk())
                return false;
        }
        return true;
    }
}
