package aood.battleship;


public class BattleshipGame {
    private Ocean ocean;
    private int turns;
    private BattleshipPlayer player;

    BattleshipGame(BattleshipPlayer player, Ocean ocean) 
    {
        this.ocean = ocean;
        turns = 0;
        this.player = player;
    }

    public int play()
    {
        turns = 0;

        player.startGame();
        ocean.placeAllBoats();
        boolean gameOver = false;

        while (turns < 100 && !gameOver) {
            Position tempPos = player.getShot();
            ocean.shoot(tempPos);
            boolean isHit = ocean.isHit(tempPos);
            char initial = ocean.getBoatInitial(tempPos);
            Boat boat = ocean.get(tempPos);
            boolean isSunk = ocean.isSunk(tempPos);
            boolean tooManyTurns = turns >= 100;
            gameOver = ocean.isAllSunk() || tooManyTurns;

            turns++;
            player.updatePlayer(tempPos, isHit, initial, boat, isSunk, gameOver, tooManyTurns, turns);
        }

        return turns;
    }
}