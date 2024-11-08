package aood.battleship;

public class AbhayNagarajStrategy extends BasePlayer{
    private boolean lookAtParent;
    private boolean lookAtChild;

    private Position lastParentShot;
    private Position lastChildShot;

    public AbhayNagarajStrategy() {
        super();
        lookAtParent = false;
        lookAtChild = false;
        lastParentShot = new Position(9, 9);
        lastChildShot = new Position(9, 9);
    }

    @Override
    public String generateName() {
        return "Abhay Nagaraj";
    }

    private Position getDirectionPosition(int amt, char direction, Position whichLastShot) {
        Position temp = null;
        if (direction == 'u') {
            temp = new Position(whichLastShot.getRowIndex() - amt, whichLastShot.getColIndex()); 
        }
        if (direction == 'd') {
            temp = new Position(whichLastShot.getRowIndex() + amt, whichLastShot.getColIndex()); 
        }
        if (direction == 'l') {
            temp = new Position(whichLastShot.getRowIndex(), whichLastShot.getColIndex() - amt); 
        }
        if (direction == 'r') {
            temp = new Position(whichLastShot.getRowIndex(), whichLastShot.getColIndex() + amt); 
        }
        return temp;
    }
    private Position checkBranch(BattleshipGrid grid, Position whichLastShot) {
        Position temp = null;
        if (whichLastShot.getRowIndex() != 0 && grid.isEmpty(getDirectionPosition(1, 'u', whichLastShot))) {
            temp = getDirectionPosition(1, 'u', whichLastShot);
        }
        else if (whichLastShot.getRowIndex() != 9 && grid.isEmpty(getDirectionPosition(1, 'd', whichLastShot))) {
            temp = getDirectionPosition(1, 'd', whichLastShot);
        }
        else if (whichLastShot.getColIndex() != 0 && grid.isEmpty(getDirectionPosition(1, 'l', whichLastShot))) {
            temp = getDirectionPosition(1, 'l', whichLastShot);
        }
        else if (whichLastShot.getColIndex() != 9 && grid.isEmpty(getDirectionPosition(1, 'r', whichLastShot))) {
            temp = getDirectionPosition(1, 'r', whichLastShot);
        }
        whichLastShot = temp;
        return temp;
    }

    private Position checkParentBranch(BattleshipGrid grid) {
        Position temp;
        temp = checkBranch(grid, lastParentShot);
        if (temp == null) {
            lookAtParent = false;
        }
        return temp;
    }

    private Position checkChildBranch(BattleshipGrid grid) {
        Position temp;
        temp = checkBranch(grid, lastChildShot);
        if (temp == null) {
            lookAtChild = false;
        }
        return temp;
    }

    private Position checkEveryOtherPosition(BattleshipGrid grid) {
        Position temp = null;
        for (int i = 0; i < grid.width(); i++) {//iterate through the whole grid
            for (int j = 0; j < grid.height(); j++) {
                if (i % 2 == 0 && j % 2 == 0) {//check every other square
                    if (grid.isEmpty(new Position(i, j))) {
                        temp = new Position(i, j);
                        break;
                    }  
                }
                else if (i % 2 != 0 && j % 2 != 0) {//check every other square but alternated
                    if (grid.isEmpty(new Position(i, j))) {
                        temp = new Position(i, j);
                        break;
                    }    
                }
            }
            if (temp != null) {
                break;
            }
        }
        lastParentShot = temp;
        return temp;
    }
    @Override
    public Position getShot() {
        BattleshipGrid grid = getGrid();
        //Position temp = new Position(0, 0);  
        Position temp = null;
        if (grid.isHit(lastParentShot)) {//if the last shot was a hit, start checking branches of that shot
            lookAtParent = true;
        }
        if (grid.isHit(lastChildShot)) {
            lookAtChild = true;
        }
        while (temp == null) {
            if (lookAtChild && grid.isHit(lastChildShot)) {
                temp = checkChildBranch(grid);
            }
            else if (lookAtParent) {
                temp = checkParentBranch(grid);
            }
            else {
                temp = checkEveryOtherPosition(grid);
            }
        } 
        return temp;
    }

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
    }

    @Override
    public void onShoot(HitInfo hitInfo) {
        System.console().printf("%s\n", getGrid());
    }

    @Override
    public void onStart() {
        //also nothing
    }
}