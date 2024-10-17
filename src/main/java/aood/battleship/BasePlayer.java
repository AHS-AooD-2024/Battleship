package aood.battleship;

public abstract class BasePlayer implements BattleshipPlayer {
    protected class HitInfo {
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
        
        public Position getPos() {
            return pos;
        }

        public Boat getBoat() {
            return boat;
        }

        public int getTurn() {
            return turn;
        }

        public boolean isGameOver() {
            return isGameOver;
        }

        public boolean isHit() {
            return boat != null;
        }

        public boolean isSunk() {
            return isHit() && boat.isSunk();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
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
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
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

        private BasePlayer getEnclosingInstance() {
            return BasePlayer.this;
        }
    }

    private String name;
    private BattleshipGrid grid;

    protected BasePlayer() {
        setName(null);
        resetGrid();
    }

    protected BasePlayer(String name) {
        setName(name);
        resetGrid();
    }

    protected BasePlayer(BattleshipGrid grid) {
        setName(null);
        resetGrid(grid);
    }

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

    protected abstract void onShoot(HitInfo hitInfo);

    // Dependency injection is love
    protected void resetGrid(BattleshipGrid grid) {
        this.grid = grid;
    }

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

    protected abstract String generateName();

    protected abstract void onStart();

    @Override
    public void updateGrid(Position pos, boolean didHit, char boatInitial) {
        grid.shoot(pos, didHit, boatInitial);
    }

    @Override
    public final void updatePlayer(Position pos, boolean isHit, char initial, Boat boat, boolean isSunk, boolean gameOver,
            boolean tooManyTurns, int turns) {
        updateGrid(pos, isHit, initial);

        onShoot(new HitInfo(pos, boat, turns, gameOver));
    }
}
