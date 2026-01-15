package marsrover.example1marsrover;

public enum Direction {
    N, E, S, W;

    public Direction turnLeft() {
        switch (this) {
            case N: return W;
            case W: return S;
            case S: return E;
            case E: return N;
        }
        throw new IllegalStateException("Invalid direction");
    }

    public Direction turnRight() {
        switch (this) {
            case N: return E;
            case E: return S;
            case S: return W;
            case W: return N;
        }
        throw new IllegalStateException("Invalid direction");
    }
}
