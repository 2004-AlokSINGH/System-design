package marsrover.example1marsrover;

public class Position {
    
    private int x;
    private int y;
    private Direction direction;

    public Position(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void moveForward() {
        switch (direction) {
            case N:
                y++;
                break;
            case S:
                y--;
                break;
            case E:
                x++;
                break;
            case W:
                x--;
                break;                                    
        }
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + direction;
    }

}
