package aood.battleship;

public class ConsolePlayer extends BasePlayer {
    public ConsolePlayer() {
        super();
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new BattleshipGridArray());
    }

    @Override
    public Position getShot() {
        Position pos = Position.getFromConsole("Shoot somewhere: ");
        while (!isValid(pos)) {
            pos = Position.getFromConsole("Invalid shot; shoot somewhere else: ");
        }
        return pos;
    }

    @Override
    protected void onShoot(HitInfo hitInfo) {
        System.console().printf("%s\n", getGrid());

        System.console().printf("Turn #%d: ", hitInfo.getTurn());

        if(hitInfo.isHit()) {
            if(hitInfo.isSunk())
                System.console().printf("You sunk their %s\n", hitInfo.getBoat().getName());
            else
                System.console().printf("You hit their %s\n", hitInfo.getBoat().getName());
        } else {
            System.console().printf("%s missed.\n", hitInfo.getPos());
        }
    }

    @Override
    protected String generateName() {
        System.console().printf("What is your name? ");
        String name = System.console().readLine();
        System.console().printf("Hello, %s. That's a good name.\n", name);
        return name;
    }

    @Override
    protected void onStart() {
        // TODO: Player place boats.
    }
    
}
