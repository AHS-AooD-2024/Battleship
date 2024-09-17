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

    public Ocean2()
    {
        BoatArrayOcean = new ArrayList<Boat>();
    }

    @Override
    public void place(Boat boat) throws BoatOverlapException, IndexOutOfBoundsException
    {
        for (Position pos : boat)
        {
            // FIXME What is going on here?
//            if (pos.equals(PositionChecker(pos)))
            if(true)
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
            if (placedBoat.onBoat(pos) && !placedBoat.isHit(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public char getBoatInitial(Position pos) {
        for (Boat placedBoat : BoatArrayOcean)
        {
           if (placedBoat.onBoat(pos))
                return placedBoat.getAbbreviation();
        }

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
}
