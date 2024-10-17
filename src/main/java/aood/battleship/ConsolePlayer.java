package aood.battleship;

import java.util.Scanner;

public class ConsolePlayer extends BasePlayer {
    private Scanner input;

    public ConsolePlayer() {
        super();
        input = new Scanner(System.in);
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
    }

    @Override
    public Position getShot() {
        return Position.getFromConsole("Shoot somewhere: ");
    }

    @Override
    protected void onShoot(HitInfo hitInfo) {
        System.out.println(getGrid());

        System.out.print("Turn #" + hitInfo.getTurn() + ": ");

        if(hitInfo.isHit()) {
            if(hitInfo.isSunk())
                System.out.println("You sunk their " + hitInfo.getBoat().getName());
            else
                System.out.println("You hit their " + hitInfo.getBoat().getName());
        } else {
            System.out.println(hitInfo.getPos() + " missed.");
        }
    }

    @Override
    protected String generateName() {
        System.out.println("What is your name? ");
        String name = input.nextLine();
        System.out.println("Hello, " + name + ". That's a good name.");
        return name;
    }

    @Override
    protected void onStart() {
        // TODO: Player place boats.
    }
    
}
