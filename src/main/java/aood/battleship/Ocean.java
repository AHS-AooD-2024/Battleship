package aood.battleship;

public interface Ocean {
    void place(Boat boat);

    default void place(Boat.Type type, Boat.Orientation orient, Position pos) {
        place(new Boat(type, pos, orient));
    }

    void shoot(Position pos);

    boolean isHit(Position pos);

    char getBoatInitial(Position pos);

    Boat get(Position pos);

    Boat.Type getBoatType(Position pos);

    String getBoatName(Position pos);

    boolean isSunk(Position pos);

    boolean isAllSunk(Position pos);
}
