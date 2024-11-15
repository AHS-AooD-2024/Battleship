package aood.battleship;

/**
 * @author Nathan Li
 */

public class NathanLiStrategy extends BasePlayer
{

    @Override
    public void resetGrid() {
        super.resetGrid(new CharArrayBattleshipGrid());
    }

    private int[] order = {4, 2, 5, 3, 3};//even nums first, ig :), if they are taken out early, then can make bigger check spaces
    private Position prevPos = new Position(0, 0);
    private int determinant = 0; //dunno what this is but it works
    private boolean hit = false;

    @Override
    public Position getShot() {
        //Position.getFromConsole("pause");
        if(hit) 
        {
            getShotSurroundings();
            timeout++;
        }
        else {
            timeout = 0;
            getShotScan();
        }
        scanTimeOut = 0;

        //has to be empty space
        if (!checkPosition(prevPos, 'e')) getShot();
        return prevPos;
    }
    private enum Direction {
        RIGHT (1, 0),
        DOWN (0, 1),
        LEFT (-1, 0),
        UP (0, -1);

        public int x, y;
        Direction(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }


    private Direction currentDirection;
    private Position origin;
    private int timeout = 0;
    private int loopTimeout = 0;

    private void getShotSurroundings()
    {
        if (timeout > 4 + orderMax() || loopTimeout > 40)
        {
            hit = false;
            timeout = 0;
            getShotScan();
            return;
        }

        int row = prevPos.getRowIndex();
        int col = prevPos.getColIndex();
        //check surroundings for hit
        //if theres hit
        //cases: prevWasHit - going right direction

        //if first shot
        if (origin == null)
        {
            origin = prevPos;
            for (Direction d : Direction.values())
            {
                if(checkPositions(prevPos, 'e', d, orderMin() / 2))
                {
                    currentDirection = d;
                    prevPos = new Position(origin.getRowIndex() + d.y, origin.getColIndex() + d.x);
                    determinant = 0;
                    break;
                }
            }
            if (checkPosition(prevPos, 'e')) return;
        }

        //if prev was a hit
        if (checkPosition(prevPos, 'h'))
        {
            prevPos = new Position(row + currentDirection.y, col + currentDirection.x);
            if (checkPosition(prevPos, 'e')) return;
        }

        row = prevPos.getRowIndex();
        col = prevPos.getColIndex();

        //if wasnt hit could went wrong direction - origin is in surroundings and it was a miss, going right orientation but wrong direction (prev was miss but origin not near)
        //if here, then prevPos was a miss and origin exists
        if (origin.equals(new Position(row - currentDirection.y, col - currentDirection.x)) || origin.equals(prevPos))
            currentDirection = Direction.values()[(currentDirection.ordinal() + 1) % 4];
        else //this means that right orientation, but wrong direction/rest is opposite side
            currentDirection = Direction.values()[(currentDirection.ordinal() + 2) % 4];
        
        prevPos = origin;
        loopTimeout++;
        getShotSurroundings();   
    }


    private boolean checkPositions(Position pos, char type, Direction d, int length)
    {
        for (int i = 1; i <= length; i++)
        {
            if (!checkPosition(new Position(
                pos.getRowIndex() + d.y * i,
                pos.getColIndex() + d.x * i
            ), type)) return false;
        }
        return true;
    }
    private boolean checkPosition(Position pos, char type)
    {
        try{

            if (type == 'e')
            {
                if (getGrid().isEmpty(pos)) return true;
            }
            else if (type == 'h')
            {
                if (getGrid().isHit(pos)) return true;
            }
            else
                if (getGrid().isMiss(pos)) return true;
        } catch (IndexOutOfBoundsException e)
        {
            return false;
        }
        return false;
    }

    private int scanTimeOut = 0;
    private boolean timeOuted = false;

    private void getShotScan()
    {
        int rowi = 0, coli = determinant, temp = orderClose();

        //have to find a better way.
        if (scanTimeOut > 100 && !timeOuted){
            for (int i = 0; i < order.length ; i++)
            {
                order[i] = 1;
            }
            timeOuted = true;
        }

        rowi = coli / 10;
        coli = coli % 10;
        //System.out.println(rowi + "-" + coli);
        
        //shrug it works :)
        if (temp != 5)
        {
            if (!timeOuted)  {
                coli += ((rowi + 1) / temp * temp - rowi);
            }
        }
        else   
        {
            coli += rowi % 5;
        }
            
        
        //wrap around
        if (coli < 0)
        {
            rowi -= 1;
            coli += 11;
        }
        //System.out.println(temp + " " + rowi + "-" + coli + "\ndeterminant " + determinant + "\nTimeout: " + scanTimeOut);
        
        prevPos = new Position(rowi, coli); //prevPos is now currentPos

        if(timeOuted) determinant -= temp;
        else determinant += temp;

        if (determinant > 100) determinant = 0;
        if (determinant < 0) determinant = 100;
        //if its not empty then go next
        if (!checkPosition(prevPos, 'e'))
        {
            scanTimeOut++;
            getShotScan();
        }
        else 
        {
            scanTimeOut = 0;
        }

        //if the ship were looking for is not possible in this location, go next
        if (!timeOuted)
        {
            boolean possible = false;
            for (Direction d : Direction.values())
            {
                if (checkPositions(prevPos, 'e', d, orderMin() / 2)){possible = true; break;}
            }
            if (!possible)
            {
                getShotScan();
            }
        }
    }
    //gets the number closest to index
    private int orderClose()
    {
        for (int num : order)
        {
            if (num != 0) return num;
        }
        //lets hope it never gets here
        return -1;
    }
    private int orderMin()
    {
        int min = 5;
        for (int num : order)
        {
            if (num < min) min = num;
        }
        return min;
    }
    private int orderMax()
    {
        int max = 2;
        for (int num : order)
        {
            if (num > max) max = num;
        }
        return max;
    }
    private void orderRemove(int length)
    {
        for (int i = 0; i < order.length; i++)
        {
            if (order[i] == length)
            {
                order[i] = 0;
                return;
            }
        }
    }

    @Override
    protected void onShoot(HitInfo hitInfo) {
        System.out.println(getGrid().toString());
        if (hitInfo.isHit()) hit = true;
        if (hitInfo.isSunk())
        {
            hit = false;
            orderRemove(hitInfo.getBoat().size());
            origin = null;
            currentDirection = null;
            timeout = 0;
            loopTimeout = 0;
        }
    }

    private static volatile long playerNum = 0;
    @Override
    protected synchronized String generateName() {
        return "NLiCPU-" + playerNum++;
    }

    @Override
    protected void onStart() {
        timeOuted = false;
        timeout = 0;
        hit = false;
        origin = null;
        currentDirection = null;
        determinant = 0;

        int[] temp = {4, 2, 5, 3, 3};
        order = temp;
    }
    public static void main(String[] args) {
        Test.main(null);
    }
    
}