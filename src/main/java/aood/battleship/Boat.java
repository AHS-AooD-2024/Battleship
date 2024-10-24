package aood.battleship;

import java.util.*;

/**
 * A boat in a game of battleship
 * 
 * @author Charush Minna
 * @author Abhay Nagaraj
 * @author Matthew Clark
 */
public class Boat implements Iterable<Position> {
    private Type type;
    private Position pos;
    private Orientation orient;
    private ArrayList<Position> hits;

    public enum Orientation {
        Horizontal,
        Vertical
        ;
    }

    public enum Type { 
        AircraftCarrier(5, 'A'), 
        Battleship(4, 'B'), 
        Cruiser(3, 'C'), 
        Destroyer(2, 'D'), 
        Submarine(3, 'S'),
        ; 

        private int size;
        private char abbr;

        private Type(int size, char abbr) {
            this.size = size;
            this.abbr = abbr;
        }

        public int size(){
            return size;
        }

        public char getAbbreviation(){
            return abbr;
        }
    }

    /**
     * Constructs a boat with a given type (e.g. Aircraft Carrier, Battleship)
     * 
     * @param type the type of the boat
     * @param pos the position of the boat
     * @param orient the orientation of boat
     */
    public Boat(Type type, Position pos, Orientation orient)
    {
        this.type = type;
        this.pos = pos;
        this.orient = orient;
        hits = new ArrayList<>(size());
    }

    /**
     * Returns the name of the type of ship
     * 
     */
    public String getName()
    {
        return type.name();
    }

    public Type getType() 
    {
        return type;
    }

    /**
     * Returns the first letter of the boat Type
     * 
     */
    public char getAbbreviation()
    {
        return type.getAbbreviation();
    }

    /**
     * Returns the size of boat based on Type
     * 
     */
    public int size()
    {
        return type.size();
    }

    /**
     * Returns boolean of whether there is a boat at the Position
     * 
     * @param target position guessed by player
     */
    public boolean onBoat(Position target)
    {
        for (Position pos : this){
            if(pos.equals(target))
                return true;
        }
        
        return false;
    }

    /**
     * Returns boolean of whether the boat is hit
     * 
     * @param target position guessed by player
     */
    public boolean isHit(Position target)
    {
        return hits.contains(target);
    }

    /**
     * Records a hit if position is guessed and ship is not yet hit
     * 
     * @param target position guessed by player
     */
    public void hit(Position target)
    {
        if(onBoat(target))
            hits.add(target);
    }

    /**
     * Returns whether a ship has been hit on all squares
     * 
     */
    public boolean isSunk()
    {
        // if the least significant n = size() bits are 1, the boat is sunk
        // return (hits + 1) == (1 << size());

        for(Position pos : this){
            if(!isHit(pos))
                return false;
        }
        return true;
        
    }

    /**
     * Returns Position where the boat will start
     * 
     */
    public Position getPosition()
    {
        return pos;
    }

    /**
     * Returns the direction of the ship
     * 
     */
    public Orientation getDirection()
    {
        return orient;
    }

    public boolean isVertical() {
        return getDirection().equals(Orientation.Vertical);
    }
    
    public boolean isHorizontal() {
        return getDirection().equals(Orientation.Horizontal);
    }

    @Override
    public Iterator<Position> iterator(){
        return new Iterator<Position>(){
            private int i = 0;
            
            @Override
            public Position next(){
                if(isHorizontal())
                    return new Position(getPosition().getRowIndex(), getPosition().getColIndex() + i++);
                else
                    return new Position(getPosition().getRowIndex() + i++, getPosition().getColIndex());
            }

            @Override
            public boolean hasNext(){
                return i < size();
            }
        };
    }

}
