package marsrover.example2mars;
// Move command
public class MoveCommand implements Command {

    @Override
    public Position execute(Position current, Plateau plateau) {
        Position next = current.moveForward();

        // Boundary check is delegated to Plateau (SRP)
        if (!plateau.isWithinBounds(next)) {
            throw new IllegalStateException("Rover cannot move outside plateau");
        }

        return next;
    }
}
