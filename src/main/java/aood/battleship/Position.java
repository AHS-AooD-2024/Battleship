package aood.battleship;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * A position in a game of battleship
 * 
 * @author Matthew Clark
 * @author Charush Minna
 */
public class Position implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * Gets the position in string form.
     * 
     * @return [row]-[column]
     */
    @Override
    public String toString() {
        return getRow() + "-" + getCol();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        return true;
    }

    // Compress the position to one byte
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte encoded = (byte)((row << 4) | (col & 0b1111));
        out.write(encoded);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        byte encoded = in.readByte();
        this.row = encoded >> 4;
        this.col = encoded & 0b1111;
    }
}
