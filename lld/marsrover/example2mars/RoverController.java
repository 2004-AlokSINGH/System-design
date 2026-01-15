package marsrover.example2mars;
// Entry point / Orchestrator
// Kept minimal intentionally
public class RoverController {

    public static void main(String[] args) {

        Plateau plateau = new Plateau(5, 5);

        Rover rover1 = new Rover(new Position(1, 2, Direction.N));
        rover1.executeCommands("LMLMLMLMM", plateau);

        Rover rover2 = new Rover(new Position(3, 3, Direction.E));
        rover2.executeCommands("MMRMMRMRRM", plateau);

        printRoverPosition(rover1);
        printRoverPosition(rover2);
    }

    // Private helper — not part of domain logic
    private static void printRoverPosition(Rover rover) {
        Position p = rover.getPosition();
        System.out.println(p.getX() + " " + p.getY() + " " + p.getDirection());
    }
}
