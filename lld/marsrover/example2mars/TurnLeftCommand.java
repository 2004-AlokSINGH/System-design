package marsrover.example2mars;


public class TurnLeftCommand implements Command {

    @Override
    public Position execute(Position current, Plateau plateau) {
        return current.turnLeft();
    }
}
