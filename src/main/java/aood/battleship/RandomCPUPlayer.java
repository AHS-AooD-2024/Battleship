package aood.battleship;

import java.util.Random;

public class RandomCPUPlayer extends BasePlayer {
    private final Random rng;

    public RandomCPUPlayer(Random rng) {
        super();
        this.rng = rng;
    }

    public RandomCPUPlayer(long seed) {
        super();
        rng = new Random(seed);
    }

    public RandomCPUPlayer() {
        rng = new Random();
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
    }

    @Override
    public Position getShot() {
        BattleshipGrid grid = getGrid();
        
        Position pos;
        do {
            pos = new Position(rng.nextInt(grid.height()), rng.nextInt(grid.width()));
        } while (!grid.isEmpty(pos));

        return pos;
    }

    @Override
    protected void onShoot(HitInfo hitInfo) {
        // The coomputer doesn't need to be told anything
    }

    private static volatile long playerNum = 0;
    @Override
    protected synchronized String generateName() {
        return "CPU-" + playerNum++;
    }

    @Override
    protected void onStart() {
        // Nothing
    }
    
}
