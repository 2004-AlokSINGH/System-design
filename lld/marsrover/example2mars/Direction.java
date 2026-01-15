package marsrover.example2mars;
// Direction is an enum because directions are a fixed, finite set
// This prevents invalid values and centralizes rotation logic
public enum Direction {

    N, E, S, W;

    // Public because direction rotation is core domain behavior
    public Direction turnLeft() {
        switch (this) {
            case N: return W;
            case W: return S;
            case S: return E;
            case E: return N;
            default: throw new IllegalStateException("Unknown direction");
        }
    }

    // Public for same reason as turnLeft
    public Direction turnRight() {
        switch (this) {
            case N: return E;
            case E: return S;
            case S: return W;
            case W: return N;
            default: throw new IllegalStateException("Unknown direction");
        }
    }
}
