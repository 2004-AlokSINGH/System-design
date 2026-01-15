package marsrover.example1marsrover;

public class CommandProcessor {
    public void process(Rover rover, String commands) {
        for (char command : commands.toCharArray()) {
            rover.executeCommand(command);
        }
    }
}
