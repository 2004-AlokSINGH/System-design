package marsrover.example2mars;

// Command abstraction
// Interface keeps rover open for new commands (OCP)
public interface Command {

    // Public because commands must be executable by Rover
    Position execute(Position current, Plateau plateau);
}
