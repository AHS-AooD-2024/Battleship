package aood.battleship;

/**
 * A position in a game of battleship
 * 
 * @author Matthew Clark
 */
public class Position {
    private int row;
    private int col;

    /**
     * Constructs a position with a given row and column (e.g. A-2)
     * 
     * @param row The row from A-J.
     * @param col The column from 1-10.
     */
    public Position(char row, int col) {
        row = Character.toUpperCase(row);
        this.row = row - 'A';
        this.col = col - 1;
    }

    /**
     * Constructs a position with given row and column indicies
     * 
     * @param rowi The row index from 0-9.
     * @param coli The column index from 0-9.
     */
    public Position(int rowi, int coli) {
        this.row = rowi;
        this.col = coli;
    }

    /**
     * Gets the index of the row of this position.
     * 
     * @return The index of the row, 0-9.
     */
    public int getRowIndex() {
        return row;
    }

    /**
     * Gets the index of the column of the position.
     * 
     * @return The index of the column, 0-9.
     */
    public int getColIndex() {
        return col;
    }

    /**
     * Gets the row.
     * 
     * @return The row, A-J
     */
    public char getRow() {
        return (char) ('A' + row);
    }

    /**
     * Gets the column.
     * 
     * @return The column, 1-10.
     */
    public int getCol() {
        return col + 1;
    }
}
