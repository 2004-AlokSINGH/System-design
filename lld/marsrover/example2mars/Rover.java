package marsrover.example2mars;
import java.util.HashMap;
import java.util.Map;

// Rover class represents a single rover entity
// Public because it is a core domain aggregate
public class Rover {

    // Private to protect rover state
    private Position position;

    // Commands map is private to prevent runtime modification
    private final Map<Character, Command> commandRegistry = new HashMap<>();

    public Rover(Position startingPosition) {
        this.position = startingPosition;

        // Command registration happens internally (encapsulation)
        commandRegistry.put('M', new MoveCommand());
        commandRegistry.put('L', new TurnLeftCommand());
        commandRegistry.put('R', new TurnRightCommand());
    }

    // Public method because controlling rover movement is core behavior
    public void executeCommands(String instructions, Plateau plateau) {
        for (char commandChar : instructions.toCharArray()) {
            Command command = commandRegistry.get(commandChar);
            if (command == null) {
                throw new IllegalArgumentException("Invalid command: " + commandChar);
            }
            position = command.execute(position, plateau);
        }
    }

    // Public getter for final output
    public Position getPosition() {
        return position;
    }
}
