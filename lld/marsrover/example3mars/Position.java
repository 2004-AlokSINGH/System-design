package marsrover.example3mars;

public final class Position {
    
    private final int x;
    private final int y;
    private final Direction direction;

    public Position(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Position moveForward() {
        switch (direction) {
            case N: return new Position(x, y + 1, direction);
            case S: return new Position(x, y - 1, direction);
            case E: return new Position(x + 1, y, direction);
            case W: return new Position(x - 1, y, direction);
            default: throw new IllegalStateException("Unknown direction");
        }
    }

    public Position turnLeft(){
        return new Position(x, y, direction.getLeftMove());
    }


    public Position turnRight() {
        return new Position(x, y, direction.getRightMove());
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

}
