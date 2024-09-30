package aood.battleship;

/**
 * A single position on the battleship grid.
 * 
 * @author Abhay Nagaraj
 */
public class GridPosition {
    private String state;
    private char hitBoat;

    /**
     * Constructs a grid position with a state and the initial of the boat which has been hit there
     * 
     * @param state denotes HIT, MISS, or EMPTY
     * @param hitBoat if the position is hit, denotes initial of boat there
     */
    public GridPosition() {
        state = "EMPTY";
        hitBoat = '0';
    }

    /**
     * Gets the state.
     * 
     * @return the state of the position
     */
    public String getState() {
        return state;
    }
    
    /**
     * Gets the initial of the boat that was hit.
     * 
     * @return the initial of the boat that was hit
     */
    public char getHitBoat() {
        return hitBoat;
    }

    /**
     * replace state with a new one
     * @param newState state that will replace the previous state
     */
    public void setState(String newState) {
        state = newState;
    }

    /**
     * signify which boat was hit
     * @param newHitBoat which boat was hit
     */
    public void setHitBoat(char newHitBoat) {
        hitBoat = newHitBoat;
    }
}
