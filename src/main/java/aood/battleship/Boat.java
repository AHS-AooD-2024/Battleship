package aood.battleship;

/**
 * A boat in a game of battleship
 * 
 * @author Charush Minna
 */
public class Boat {
    private Type type;
    private Position pos;
    private String orient;

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
    public Boat(Type type, Position pos, String orient)
    {
        this.type = type;
        this.pos = pos;
        this.orient = orient;
    }

    /**
     * Returns the name of the type of ship
     * 
     */
    public String getName()
    {
        return type.name();
    }

    public Type getType() {
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
    public size()
    {
        return type.size();
    }

    /**
     * Returns boolean of whether there is a boat at the Position
     * 
     * @param target position guessed by player
     */
    public onBoat(Position target)
    {
        
    }

    /**
     * Returns boolean of whether the boat is hit
     * 
     * @param target position guessed by player
     */
    public isHit(Position target)
    {
        
    }

    /**
     * Records a hit if position is guessed and ship is not yet hit
     * 
     * @param target position guessed by player
     */
    public hit(Position target)
    {
        
    }

    /**
     * Returns whether a ship has been hit on all squares
     * 
     */
    public sunk()
    {
        
    }

    /**
     * Returns Position where the boat will start
     * 
     */
    public position()
    {
        
    }

    /**
     * Returns the direction of the ship
     * 
     */
    public direction()
    {
        
    }

}