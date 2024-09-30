package aood.battleship;

public class BattleshipGridArray implements BattleshipGrid {
    private GridPosition[][] grid;
    final int ROW = 10;
    final int COL = 10;
    
    public BattleshipGridArray() {
        grid = new GridPosition[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                grid[ROW][COL] = new GridPosition();
            }
        }
    }

    @Override
    public void shoot(Position pos, boolean isHit, char boatInitial) {
        if (isHit) {
            int tempRow = pos.getRowIndex();
            int tempCol = pos.getColIndex();
            grid[tempRow][tempCol].setState("HIT");
            grid[tempRow][tempCol].setHitBoat(boatInitial);
        }
        else {
            int tempRow = pos.getRowIndex();
            int tempCol = pos.getColIndex();
            grid[tempRow][tempCol].setState("MISS");
        }
    }
    @Override
    public boolean isHit(Position pos) {
        int tempRow = pos.getRowIndex();
        int tempCol = pos.getColIndex();
        if (grid[tempRow][tempCol].getState().equals("HIT")) {
            return true;
        }
        return false;
    }
    @Override
    public boolean isMiss(Position pos) {
        int tempRow = pos.getRowIndex();
        int tempCol = pos.getColIndex();
        if (grid[tempRow][tempCol].getState().equals("MISS")) {
            return true;
        }
        return false;
    }
    @Override
    public boolean isEmpty(Position pos) {
        int tempRow = pos.getRowIndex();
        int tempCol = pos.getColIndex();
        if (grid[tempRow][tempCol].getState().equals("EMPTY")) {
            return true;
        }
        return false;
    }
    @Override
    public char getBoatInitial(Position pos) {
        return grid[pos.getRowIndex()][pos.getColIndex()].getHitBoat();
    }
}
