
package aood.battleship;

public class CharushMinnaStrategy extends BasePlayer {

    int currentRow;
    int currentCol;
    Position tempPos;
    boolean lastShotHit;
    int adjacentIndex = 0; // Track which adjacent cell to check next
    boolean fullTraversalMode = false; // Flag for full traversal after completing checkerboard pattern

    
    public CharushMinnaStrategy() {
        super();
        tempPos = new Position(0, 0);
        currentRow = 0;
        currentCol = 0;
        lastShotHit = false;
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
        fullTraversalMode = false; // Reset flag whenever we reset the grid
    }

    @Override
    public Position getShot() {
        //Position.getFromConsole();
        System.out.println(tempPos);
        if (getGrid().isHit(tempPos)) {
            Position nextAdjacent = checkAdjacentPositions();
            if (nextAdjacent != null) {
                //tempPos = nextAdjacent;
                return nextAdjacent;
            }
        }
        return checkEveryOtherPosition(); // Go back to main pattern if no adjacents are available
    }

    public Position checkEveryOtherPosition() {
        Position pos = new Position(currentRow, currentCol);
        

        do {
            if (!fullTraversalMode) {
                currentCol += 2;
            
                if (currentCol >= getGrid().width()) {
                    currentCol = (currentCol % 2 == 0) ? 1 : 0;    
                    currentRow++;       
                }

                if (currentRow >= getGrid().height()) {
                    fullTraversalMode = true;
                    currentRow = 0;
                    currentCol = 0;
                }
            
            } else {
                // Full traversal: increment every cell without skipping
                pos = new Position(currentRow, currentCol);
                currentCol++;
                
                if (currentCol >= getGrid().width()) {
                    currentCol = 0;
                    currentRow++;
                }

                // If the entire grid has been traversed, restart at the beginning
                if (currentRow >= getGrid().height()) {
                    currentRow = 0;
                    currentCol = 0;
                }
            }
            
        } while (!getGrid().isEmpty(pos) && (currentRow < getGrid().height()));
        
        tempPos = pos;
        return pos;
    }

    private Position checkAdjacentPositions() {
        // Define the four possible directions around `tempPos`
        Position[] adjacentPositions = new Position[] {
            new Position(tempPos.getRowIndex(), tempPos.getColIndex() + 1), // Up
            new Position(tempPos.getRowIndex() - 1, tempPos.getColIndex()), // Left
            new Position(tempPos.getRowIndex() + 1, tempPos.getColIndex()), // Right
            new Position(tempPos.getRowIndex(), tempPos.getColIndex() - 1)  // Down
        };

        // Check each adjacent position, starting from where we last left off
        while (adjacentIndex < adjacentPositions.length) {
            Position pos = adjacentPositions[adjacentIndex];
            adjacentIndex++; // Move to the next direction for the next call

            // Check if the position is within bounds and empty, if so, return it
            if (isWithinBounds(pos) && getGrid().isEmpty(pos)) {
                return pos;
            }
        }

        // If all adjacent positions are checked, reset index and return null
        adjacentIndex = 0;
        return null;
    }
    
    // Helper method to check if a position is within the getGrid() bounds
    private boolean isWithinBounds(Position pos) {
        int row = pos.getRowIndex();
        int col = pos.getColIndex();
        return row >= 0 && row < getGrid().height() && col >= 0 && col < getGrid().width();
    }

    @Override
    protected void onShoot(HitInfo hitInfo) {
        // Confirm onShoot is called and lastShotHit is updated correctly
        System.out.println("onShoot called with hitInfo at position: " + hitInfo.getPos());
    
        // Update lastShotHit based on whether the shot was a hit
        lastShotHit = hitInfo.isHit();
        System.out.println("Hit status: " + lastShotHit);
    
        if (lastShotHit) {
            System.out.println("Hit recorded! Setting tempPos to: " + tempPos);
        } else {
            System.out.println("Miss recorded.");
        }
    
        // Print the grid to see the current state of hits and misses
        System.out.println(getGrid());
    }

    @Override
    protected String generateName() {
        return "ADV";
    }

    @Override
    protected void onStart() {
        tempPos = new Position(0, 0);
        currentRow = 0;
        currentCol = 0;
        lastShotHit = false;
        fullTraversalMode = false; // Reset the flag on each start
    }
}
