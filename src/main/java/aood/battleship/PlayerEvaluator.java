package aood.battleship;

public class PlayerEvaluator {

    private int totalTurns = 0;
    private int minTurns = 100;
    private int maxTurns = 0;
    private double averageTurns = 0.0;

    private BattleshipPlayer player;
    private int runs;
    
    private int totalTurnsWhenWinning;
    private int wins;

    private double winRate;
    private double averageTurnsWhenWinning;

    public PlayerEvaluator(BattleshipPlayer player, int runs) {
        this.player = player;
        this.runs = runs;
    }

    public void evaluate() {
        for (int i = 0; i < runs; i++)
        {
            BoatArrayOcean bao = new BoatArrayOcean();
            BattleshipGame game = new BattleshipGame(player, bao);
            int turns = game.play();
            totalTurns += turns;
            if(turns < 100) {
                totalTurnsWhenWinning += turns;
                wins++;
            }
            minTurns = Math.min(minTurns, turns);
            maxTurns = Math.max(maxTurns, turns);
        }

        winRate = (double) wins / runs;

        averageTurnsWhenWinning = (double) totalTurnsWhenWinning / runs;

        averageTurns = (double) totalTurns / runs;
    }

    public int maxTurns() {
        return maxTurns;
    }

    public int minTurns() {
        return minTurns;
    }

    public double averageTurns() {
        return averageTurns;
    }

    public double winRate() {
        return winRate;
    }

    public double averageTurnsWhenWinning() {
        return averageTurnsWhenWinning;
    }

}