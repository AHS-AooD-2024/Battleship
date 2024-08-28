package aood.battleship;

/**
 * A PositionChecker for battleship within A-J / 1-10
 * 
 * @author Nathan Li
 */
public class PositionChecker
{
    /**
     * Checks if a position is valid within A-J & 1-10
     *  
     * @param inputPos a position in format row-column eg. (A-1)
     * @return a Position object. Invalid position returns Position indicies (-1,-1)
     */
    public static Position checkPosition(String inputPos)
    {
        String[] input = inputPos.split("-");
        
        if (input.length == 2)
        {
            char row = input[0].charAt(0);
            int col;
            try {

                col = Integer.parseInt(input[1]);

            } catch (Exception e) {

                return new Position(-1, -1);
                
            }
            

            if (row - 'A' >= 0 && 
                row - 'A' < 10 &&
                col > 0 &&
                col <= 10
                )
            {
                return new Position(row, col);
            }
        }

        //return for invalid format
        return new Position(-1, -1);
    }

    /* 
    public static void main (String[] args)
    {
        System.out.println(PositionChecker.checkPosition("@-9"));
        System.out.println(PositionChecker.checkPosition(""));
    }
    */
    
}