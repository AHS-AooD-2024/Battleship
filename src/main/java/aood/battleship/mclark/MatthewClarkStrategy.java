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
        return elvis(moves.poll(), this::mostLikely);
    }

    private Position lastShot;
    private boolean lastShotDidHit;

    @Override
    protected void onShoot(HitInfo hitInfo) {
        // System.out.println("Shot: " + hitInfo.getPos());
        // for(int r = 0; r < 10; r++) {
        //     for(int c = 0; c < 10; c++) {
        //         System.out.print(chance[r][c]);
        //     }
        //     System.out.println();
        // }
        // System.out.println();

        // System.out.println(getGrid());
        // System.out.println();

        if(hitInfo.isHit()) {
            moves.clear();

            if(hitInfo.isSunk()) {
                moves.clear();
                moves.offer(mostLikely());
            } else if(lastShotDidHit) {
                // FIXME this is bad and doesn't work.
                // suppossed to continue shooting in the same direction until
                // sunk
                // also should go to the oposite side if we miss and the boat is
                // not sunk. That is why the previous if just cancels everything if we sink the boat.
                int rowDiff = hitInfo.getPos().getRowIndex() - lastShot.getRowIndex();
                int colDiff = hitInfo.getPos().getColIndex() - lastShot.getColIndex();
            
                Position next = new Position(
                    hitInfo.getPos().getRowIndex() + rowDiff, 
                    hitInfo.getPos().getColIndex() + colDiff 
                );

                if(goodToShoot(next))
                    moves.offer(next);
            } else {
                // This is if this was the discovery shot of a boat.
                Position up = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() - 1);
                Position left = new Position(hitInfo.getPos().getRowIndex() - 1, hitInfo.getPos().getColIndex());
                Position right = new Position(hitInfo.getPos().getRowIndex() + 1, hitInfo.getPos().getColIndex());
                Position down = new Position(hitInfo.getPos().getRowIndex(), hitInfo.getPos().getColIndex() + 1);

                if(goodToShoot(up)) moves.offer(up);
                if(goodToShoot(left)) moves.offer(left);
                if(goodToShoot(right)) moves.offer(right);
                if(goodToShoot(down)) moves.offer(down);
            }
        } else {
            if(!lastShotDidHit)
                moves.offer(mostLikely());
        }
        lastShot = hitInfo.getPos();
        lastShotDidHit = hitInfo.isHit();
        reprobulate();
    }

    private Position mostLikely() {
        int bestR = 0;
        int bestC = 0;
        // O(n^2) unfortunately
        for(int r = 1; r < chance.length; r++) {
            for(int c = 0; c < chance[0].length; c++) {
                if(goodToShoot(new Position(r, c)) && chance[r][c] > chance[bestR][bestC]) {
                    bestR = r;
                    bestC = c;
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
            if(getGrid().isMiss(new Position(r, c))) {
                chance[r][c] = 0;
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
            if(getGrid().isMiss(new Position(r, c))) {
                chance[r][c] = 0;
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