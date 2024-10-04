package aood.battleship;

/**
 * A {@link BattleshipGrid} implementation using a 2d array of
 * {@code char}s. This grid will indicate empty squares with a space
 * (so that the square is empty), and missed squares with an asterisk
 * ({@code *}).
 *
 * @author Matthew Clark
 */
public class CharArrayBattleshipGrid implements BattleshipGrid {
    private static final int ROWS = 10;
    private static final int COLS = 10;

    private static final char EMPTY = ' ';
    private static final char MISS = '*';

    private final char[][] grid;

    public CharArrayBattleshipGrid() {
        grid = new char[ROWS][COLS];

        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                grid[r][c] = EMPTY;
            }
        }
    }

    private char status(boolean isHit, char ch) {
        return isHit ? ch : MISS;
    }

    @Override
    public void shoot(Position pos, boolean isHit, char boatInitial) {
        grid[pos.getRowIndex()][pos.getColIndex()] = status(isHit, boatInitial);
    }

    @Override
    public boolean isHit(Position pos) {
        char ch = get(pos);
        return ch != EMPTY && ch != MISS;
    }

    @Override
    public boolean isMiss(Position pos) {
        return get(pos) == MISS;
    }

    @Override
    public boolean isEmpty(Position pos) {
        return get(pos) == EMPTY;
    }

    private char get(Position pos) {
        return grid[pos.getRowIndex()][pos.getColIndex()];
    }

    @Override
    public char getBoatInitial(Position pos) {
        char ch = get(pos);
        return ch == EMPTY || ch == MISS ? '0' : ch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("+ 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10+\n");
        for(int row = 0; row < ROWS; row++) {
            sb.append((char)(row + 'A'));
            for(int col = 0; col < COLS; col++) {
                Position here = new Position(row, col);

                char ch = get(here);
                // Replace null with space
                ch = ch != 0 ? ch : ' ';

                sb.append(' ').append(ch).append(" |");
            }
            sb.append("\n+ - + - + - + - + - + - + - + - + - + - +\n");
        }

        return sb.toString();
    }
}
