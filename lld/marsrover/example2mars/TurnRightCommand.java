package marsrover.example2mars;

public class TurnRightCommand implements Command {

    @Override
    public Position execute(Position current, Plateau plateau) {
        return current.turnRight();
    }
}
