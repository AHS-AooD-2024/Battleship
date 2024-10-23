package aood.battleship;

/**
 * An {@link BattleshipGrid} implementation for battleship.
 * 
 * @author Abhay Nagaraj
 */
public class BattleshipGridArray implements BattleshipGrid {
    private GridPosition[][] grid;
    final int ROW = 10;
    final int COL = 10;
    
    public BattleshipGridArray() {
        grid = new GridPosition[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                grid[i][j] = new GridPosition();
            }
        }
    }

    @Override
    public void shoot(Position pos, boolean isHit, char boatInitial) {
        int tempRow = pos.getRowIndex();
        int tempCol = pos.getColIndex();
        if (isHit) {
            grid[tempRow][tempCol].setState("HIT");
            grid[tempRow][tempCol].setHitBoat(boatInitial);
        }
        else {
            grid[tempRow][tempCol].setState("MISS");
            grid[tempRow][tempCol].setHitBoat('*');
        }
    }
    @Override
    public boolean isHit(Position pos) {
        return isBlank(pos, "HIT");
    }
    @Override
    public boolean isMiss(Position pos) {
        return isBlank(pos, "MISS");
    }
    @Override
    public boolean isEmpty(Position pos) {
        return isBlank(pos, "EMPTY");
    }

    /**
     * Streamlines is___ methods
     * @param pos position passed into is___ method
     * @param state state tested for in is___ method
     * @return whether the chosen position is in the checked state
     */
    private boolean isBlank(Position pos, String state) {
        int tempRow = pos.getRowIndex();
        int tempCol = pos.getColIndex();
        if (grid[tempRow][tempCol].getState().equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public char getBoatInitial(Position pos) {
        return grid[pos.getRowIndex()][pos.getColIndex()].getHitBoat();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("   1 2 3 4 5 6 7 8 9 10\n");
        for(int row = 0; row < ROW; row++) {
            sb.append((char)(row + 'A') + " ");
            for(int col = 0; col < COL; col++) {
                Position here = new Position(row, col);

                char ch = getBoatInitial(here);
                // Replace null with space
                ch = ch != 0 ? ch : ' ';

                sb.append(' ').append(ch);
            }
            sb.append("\n\n");
        }

        return sb.toString();
    }
}
