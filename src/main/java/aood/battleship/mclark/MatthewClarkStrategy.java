package aood.battleship.mclark;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;

import aood.battleship.BasePlayer;
import aood.battleship.BattleshipGrid;
import aood.battleship.CharArrayBattleshipGrid;
import aood.battleship.Position;

public class MatthewClarkStrategy extends BasePlayer {

    private int[][] chance;

    private Queue<Position> moves;

    public MatthewClarkStrategy() {
        super();
        moves = new PriorityQueue<Position>((a, b) -> Integer.compare(chance[a.getRowIndex()][a.getColIndex()], chance[b.getRowIndex()][b.getColIndex()]));
        chance = new int[10][10];
        reprobulate();
    }

    // ?: from kt
    private <T> T elvis(T thing, T alternative) {
        return thing != null ? thing : alternative;
    }

    // lazy version
    private <T> T elvis(T thing, Supplier<T> alternative) {
        return thing != null ? thing : alternative.get();
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
    }

    @Override
    public Position getShot() {
        Position shot;
        if(!goodToShoot(shot = elvis(moves.poll(), this::mostLikely))) {
            System.out.println("Repeat shot: " + shot);
        }

        if((mode & ALL_SEARCH) != 0) {
            if(shot.equals(up))
                currentDirection = UP_SEARCH;
            else if(shot.equals(down))
                currentDirection = DOWN_SEARCH;
            else if(shot.equals(left))
                currentDirection = LEFT_SEARCH;
            else if(shot.equals(right))
                currentDirection = RIGHT_SEARCH;
        }

        return shot;
    }

    private Position lastShot;
    private boolean lastShotDidHit;
    private int mode;

    private static final int MOST_LIKELY =  1 << 0;
    private static final int LEFT_SEARCH =  1 << 1;
    private static final int RIGHT_SEARCH = 1 << 2;
    private static final int UP_SEARCH =    1 << 3;
    private static final int DOWN_SEARCH =  1 << 4;
    private static final int HORIZONTAL_SEARCH = LEFT_SEARCH | RIGHT_SEARCH;
    private static final int VERTICAL_SEARCH = UP_SEARCH | DOWN_SEARCH;
    private static final int ALL_SEARCH = HORIZONTAL_SEARCH | VERTICAL_SEARCH;

    private Position up;
    private Position right;
    private Position down;
    private Position left;

    private int currentDirection;

    @Override
    protected void onShoot(HitInfo hitInfo) {
        reprobulate();

        // System.out.println("Shot: " + hitInfo.getPos());
        // for(int r = 0; r < 10; r++) {
        //     for(int c = 0; c < 10; c++) {
        //         System.out.printf("%3d", chance[r][c]);
        //     }
        //     System.out.println();
        // }
        // System.out.println();

        // System.out.println(getGrid());
        // System.out.println();

        if(hitInfo.isHit()) {
            moves.clear();

            if(hitInfo.isSunk()) {
                mode = MOST_LIKELY;
                moves.clear();
                moves.offer(mostLikely());
            } else if(lastShotDidHit && (mode & ALL_SEARCH) != 0) {
                // use current direction
                moves.clear();

                Position _up = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() - 1);
                Position _left = new Position(hitInfo.getPos().getRowIndex() - 1, hitInfo.getPos().getColIndex());
                Position _right = new Position(hitInfo.getPos().getRowIndex() + 1, hitInfo.getPos().getColIndex());
                Position _down = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() + 1);

                Position finale = null;

                switch (currentDirection) {
                    case UP_SEARCH:
                        finale = _up;
                        break;
                    case DOWN_SEARCH:
                        finale = _down;
                        break;
                    case LEFT_SEARCH:
                        finale = _left;
                        break;
                    case RIGHT_SEARCH:
                        finale = _right;
                        break;
                
                    default:
                        throw new IllegalStateException("Current search direction " + currentDirection);
                }

                if(goodToShoot(finale)) {
                    moves.offer(finale);
                } else {
                    mode &= ~currentDirection;
                }

            } else {
                // This is if this was the discovery shot of a boat.
                up = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() - 1);
                left = new Position(hitInfo.getPos().getRowIndex() - 1, hitInfo.getPos().getColIndex());
                right = new Position(hitInfo.getPos().getRowIndex() + 1, hitInfo.getPos().getColIndex());
                down = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() + 1);

                if(goodToShoot(up)){ 
                    moves.offer(up);
                    mode |= UP_SEARCH;
                }

                if(goodToShoot(left)){
                    moves.offer(left);
                    mode |= LEFT_SEARCH;
                }

                if(goodToShoot(right)) {
                    moves.offer(right);
                    mode |= RIGHT_SEARCH;
                }
                if(goodToShoot(down)) {
                    moves.offer(down);
                    mode |= DOWN_SEARCH;
                }
            }
        } else {
            // if miss
            
            // remove the direction from searches
            mode &= ~currentDirection;
            
        }
        lastShot = hitInfo.getPos();
        lastShotDidHit = hitInfo.isHit();

        // System.console().readLine("Enter to coninue.");
    }

    private Position mostLikely() {
        int bestR = 0;
        int bestC = 0;

        int bestChance = Integer.MIN_VALUE;

        int r = 0, c = 0;
        boolean superBreak = true;

        for(r = 0; r < chance.length && superBreak; r++) {
            for(c = 0; c < chance[0].length; c++) {
                if(goodToShoot(new Position(r, c))) {
                    bestR = r;
                    bestC = c;
                    bestChance = chance[bestR][bestC];
                    superBreak = false;
                    break;
                }
            }
        }
        
        // O(n^2) unfortunately
        for(; r < chance.length; r++) {
            for(c = 0; c < chance[0].length; c++) {
                if(chance[r][c] >= bestChance){
                    if(goodToShoot(new Position(r, c))) {
                        bestR = r;
                        bestC = c;
                        bestChance = chance[bestR][bestC];
                    }
                    // else {
                    //     System.out.println("Rejected " + new Position(r, c));
                    // }
                }
            }
        }
        return new Position(bestR, bestC);
    }

    /**
     * Repopulates the probability map given the information of the grid.
     */
    private void reprobulate() {
        resetProbabilities();

        final int[] shipsWithLength = {0, 1, 2, 1, 1};

        for(int r = 0; r < chance.length; r++) {
            horizontalReprobulate(shipsWithLength, r);
        }

        for(int c = 0; c < chance.length; c++) {
            verticalReprobulate(shipsWithLength, c);
        }
    }

    private void horizontalReprobulate(final int[] shipsWithLength, int r) {
        for(int c = 0; c < chance[0].length - 1; c++) {
            // the general algorithm doesn't do this, so we'll manually
            // zero out already shot positions
            if(!getGrid().isEmpty(new Position(r, c))) {
                chance[r][c] = -99;
                continue;
            }
            for(int search = 1; search < shipsWithLength.length; search++) {
                // end of the grid, go to next col
                if(c + search >= chance[0].length) break;
                // blocked square, search from next col
                if(getGrid().isMiss(new Position(r, c + search))) break;
                // both of these are required
                chance[r][c + search] += shipsWithLength[search];
                chance[r][c] += shipsWithLength[search];
            }
        }
    }
    
    private void verticalReprobulate(final int[] shipsWithLength, int c) {
        for(int r = 0; r < chance.length - 1; r++) {
            // the general algorithm doesn't do this, so we'll manually
            // zero out already shot positions
            if(!getGrid().isEmpty(new Position(r, c))) {
                chance[r][c] = -99;
                continue;
            }

            for(int search = 1; search < shipsWithLength.length; search++) {
                // end of the grid, go to next row
                if(r + search >= chance.length) break;
                // blocked square, search from next row
                if(getGrid().isMiss(new Position(r + search, c))) break;

                // both of these are required
                chance[r + search][c] += shipsWithLength[search];
                chance[r][c] += shipsWithLength[search];
            }
        }
    }

    private void resetProbabilities() {
        for(int r = 0; r < chance.length; r++) {
            for(int c = 0; c < chance.length; c++) {
                chance[r][c] = 0;
            }
        }
    }
            
    private boolean goodToShoot(Position pos) {
        return isValid(pos) && getGrid().isEmpty(pos);
    }

    @Override
    protected String generateName() {
        return "John Jacob Jingleheimer-Schmidt";
    }

    @Override
    protected void onStart() {
        // System.out.println();
        // for(int r = 0; r < 10; r++) {
        //     for(int c = 0; c < 10; c++) {
        //         System.out.format("%3d", chance[r][c]);
        //     }
        //     System.out.println();
        // }
        reprobulate();
        moves.clear();
    }

    
}