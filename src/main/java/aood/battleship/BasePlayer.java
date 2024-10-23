package aood.battleship;

/**
 * A basic player to ease implementation of other players.
 * 
 * @author Matthew Clark
 */
public abstract class BasePlayer implements BattleshipPlayer {
    /**
     * A set of information on a hit.
     * 
     * @author Matthew Clark
     */
    protected static class HitInfo {
        private final Position pos;
        private final Boat boat;
        private final int turn;
        private final boolean isGameOver;
        
        public HitInfo(Position pos, Boat boat, int turn, boolean isGameOver) {
            this.pos = pos;
            this.boat = boat;
            this.turn = turn;
            this.isGameOver = isGameOver;
        }
        
        /**
         * The position fired upon.
         * 
         * @return The position fired upon.
         */
        public Position getPos() {
            return pos;
        }

        /**
         * The boat fired upon, or {@code null} if there is none.
         * 
         * @return The boat fired upon.
         */
        public Boat getBoat() {
            return boat;
        }

        /**
         * Gets the turn this information is from.
         * 
         * @return The turn of the game.
         */
        public int getTurn() {
            return turn;
        }

        /**
         * Checks if this turn ended the game.
         * 
         * @return {@code true} if the game is now over, {@code false} otherwise.
         */
        public boolean isGameOver() {
            return isGameOver;
        }

        /**
         * Checks if the game ended by length rather than sinkage.
         * 
         * @return {@code true} id the game is over without sinking a boat.
         */
        public boolean isTooManyTurns() {
            return isGameOver && !isSunk();
        }

        /**
         * Checks if this shot actually hit anything.
         * 
         * @return {@code true} if a boat was hit, {@code false} otherwise.
         */
        public boolean isHit() {
            return boat != null;
        }

        /**
         * Checks if this shot sunk the boat.
         * 
         * @return {@code true} if a boat was sunk, {@code false} otherwise.
         */
        public boolean isSunk() {
            return isHit() && boat.isSunk();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((pos == null) ? 0 : pos.hashCode());
            result = prime * result + ((boat == null) ? 0 : boat.hashCode());
            result = prime * result + turn;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            HitInfo other = (HitInfo) obj;
            if (pos == null) {
                if (other.pos != null)
                    return false;
            } else if (!pos.equals(other.pos))
                return false;
            if (boat == null) {
                if (other.boat != null)
                    return false;
            } else if (!boat.equals(other.boat))
                return false;
            if (turn != other.turn)
                return false;
            return true;
        }
    }

    private String name;
    private BattleshipGrid grid;

    /**
     * Initializes with setting name to null.
     */
    protected BasePlayer() {
        setName(null);
        resetGrid();
    }

    /**
     * Initializes this player with a given name.
     * 
     * @param name
     */
    protected BasePlayer(String name) {
        setName(name);
        resetGrid();
    }

    /**
     * initializes this player with a given grid.
     * 
     * @param grid The grid that this player will use.
     */
    protected BasePlayer(BattleshipGrid grid) {
        setName(null);
        resetGrid(grid);
    }

    /**
     * Initializes this player.
     * 
     * @param name The name of the player.
     * @param grid The grid this player will use.
     */
    protected BasePlayer(String name, BattleshipGrid grid) {
        setName(name);
        this.grid = grid;
    }

    @Override
    public BattleshipGrid getGrid() {
        return grid;
    }

    @Override
    public abstract void resetGrid();

    @Override
    public abstract Position getShot();

    /**
     * Called when the player recieves information about their shot.
     * 
     * @param hitInfo The aforementioned information.
     */
    protected abstract void onShoot(HitInfo hitInfo);

    // Dependency injection is love
    protected void resetGrid(BattleshipGrid grid) {
        this.grid = grid;
    }

    /**
     * Sets the players name.
     * 
     * @param name The player's new name.
     */
    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public final void startGame() {
        if(name == null) {
            name = generateName();
        }

        resetGrid();
        onStart();
    }

    /**
     * Generates a name for this player, either from input or something else.
     * This method is only called when a game is started with a {@code null} name.
     * 
     * @return The generated name;
     */
    protected abstract String generateName();

    /**
     * Called at the beginning of the game, before any turns are taken.
     */
    protected abstract void onStart();

    @Override
    public void updateGrid(Position pos, boolean didHit, char boatInitial) {
        grid.shoot(pos, didHit, boatInitial);
    }

    @Override
    public final void updatePlayer(Position pos, boolean isHit, char initial, Boat boat, boolean isSunk, boolean gameOver,
            boolean tooManyTurns, int turns) {
        System.out.println("Position in BasePlayer: " + pos);
        updateGrid(pos, isHit, initial);

        onShoot(new HitInfo(pos, boat, turns, gameOver));
    }
}
