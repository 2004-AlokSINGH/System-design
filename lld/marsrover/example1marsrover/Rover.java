package marsrover.example1marsrover;

public class Rover {

    private Position position;
    private Pleateau plateau;

    public Rover(Position position, Pleateau plateau) {
        this.position = position;
        this.plateau = plateau;
    }

    public void executeCommand(char command) {
        switch (command) {
            case 'L': position.turnLeft(); break;
            case 'R': position.turnRight(); break;
            case 'M':
                Position old = new Position(position.getX(), position.getY(), position.getDirection());
                position.moveForward();
                if (!plateau.isWithinBounds(position.getX(), position.getY())) {
                    position = old; // Prevent moving out of bounds
                }
                break;
        }
    }

    public Position getPosition() {
        return position;
    }
}
