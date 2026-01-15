package marsrover.example3mars;

public class CommandProcessor {

    public Position processCommand(Rover r, String commandSeq, Pleatue pleatue) {
        Position finalPosition = r.getPosition();
        for (char command : commandSeq.toCharArray()) {
            switch (command) {
                case 'L':
                    finalPosition = finalPosition.turnLeft();
                    break;
                case 'R':
                    finalPosition = finalPosition.turnRight();
                    break;
                case 'M':
                    Position position = finalPosition.moveForward();
                    if (pleatue.isValid(position.getX(), position.getY())) {
                        finalPosition = position;
                    } else {
                        throw new IllegalStateException("Out of pleatau");
                    }
                    break;
                default:
                    throw new IllegalStateException("unkwon dir");

            }

        }
        return finalPosition;
    }

}
