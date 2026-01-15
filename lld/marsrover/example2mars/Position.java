package marsrover.example2mars;

// Immutable value object representing rover position
// Marked final to prevent subclassing (protects invariants)
public final class Position {

    // Private to enforce encapsulation and immutability
    private final int x;
    private final int y;
    private final Direction direction;

    // Public constructor as this is a core domain object
    public Position(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // Public getters allow read-only access
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    // Creates a new Position instead of mutating (immutability)
    public Position moveForward() {
        switch (direction) {
            case N: return new Position(x, y + 1, direction);
            case S: return new Position(x, y - 1, direction);
            case E: return new Position(x + 1, y, direction);
            case W: return new Position(x - 1, y, direction);
            default: throw new IllegalStateException("Unknown direction");
        }
    }

    public Position turnLeft() {
        return new Position(x, y, direction.turnLeft());
    }

    public Position turnRight() {
        return new Position(x, y, direction.turnRight());
    }
}
