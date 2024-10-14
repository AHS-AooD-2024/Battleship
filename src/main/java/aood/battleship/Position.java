package aood.battleship;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A position in a game of battleship
 * 
 * @author Matthew Clark
 * @author Charush Minna
 * @author Abhay Nagaraj
 * 
 */
public class Position implements Serializable {
    // @Serial
    private static final long serialVersionUID = 4L;

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
     * Constructs a position at {@code (0, 0)}.
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Gets a position value from an input stream.
     * 
     * @param in The stream to take input from. If it is an {@link ObjectInputStream}, serialization
     * as specified by {@link #readObject(ObjectInputStream)} will be preformed, otherwise, a line
     * of the stream will be read and parsed by {@link PositionChecker#checkPosition(String)}.
     * @return A position as parsed. Will return a {@code (-1, -1)} position.
     */
    public static Position get(InputStream in) {
        if (in == null) {
            throw new NullPointerException("Input cannot be null.");
        }

        if(in instanceof ObjectInputStream){
            // Standard serialization
            Position pos = new Position();
            try {
                pos.readObject((ObjectInputStream) in);
            } catch (ClassNotFoundException | IOException e) {
                // Reusing the already allocated position
                pos.col = -1;
                pos.row = -1;
                return pos;
            }
            return pos;
        } else {
            // Try to read a string I guess
            try (Scanner scanner = new Scanner(in)) {
                return PositionChecker.checkPosition(scanner.nextLine());
            } catch (NoSuchElementException e) {
                // Error value
                return new Position(-1, -1);
            }
        }
    }

    /**
     * Gets a position from {@link System#in} after outputting a message to 
     * {@link System#out}.
     * <p>
     * {@link System#setIn(InputStream)} and {@link System#setOut(java.io.PrintStream)}
     * can be used to change the behaviour of this method.
     * 
     * @param message A prompt message formatted with args.
     * @param args Format arguments
     * @return A position parsed from system input.
     */
    public static Position getFromConsole(String message, Object... args) {
        System.out.format(message, args);
        return getFromConsole();
    }

    /**
     * Gets a position from {@link System#in}.
     * <p>
     * {@link System#setIn(InputStream)} can be used to change the behaviour 
     * of this method.
     * 
     * @return A position parsed from system input.
     */
    public static Position getFromConsole() {
        return get(System.in);
    }

    // public Position() {
    //     Scanner input = new Scanner(System.in);
    //     System.out.print("Enter a position: ");
    //     Position tempPos = PositionChecker.checkPosition(input.nextLine());
    //     row = tempPos.getRowIndex();
    //     col = tempPos.getColIndex();
    //     input.close();
    // }

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
