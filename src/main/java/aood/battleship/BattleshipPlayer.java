package aood.battleship;

public interface BattleshipPlayer {
    void startGame();

    String getName();

    Position getShot();

    void updateGrid(Position pos, boolean didHit, char boatInitial);

    BattleshipGrid getGrid();

    void resetGrid();

    void updatePlayer(Position pos, boolean isHit, char initial, Boat boat, boolean isSunk, boolean gameOver, boolean tooManyTurns, int turns);
}
