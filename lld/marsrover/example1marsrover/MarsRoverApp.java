package marsrover.example1marsrover;

import java.util.Scanner;

public class MarsRoverApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Plateau size
        int maxX = sc.nextInt();
        int maxY = sc.nextInt();
        Pleateau plateau = new Pleateau(maxX, maxY);

        CommandProcessor processor = new CommandProcessor();

        while (sc.hasNext()) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Direction dir = Direction.valueOf(sc.next());
            Position position = new Position(x, y, dir);
            Rover rover = new Rover(position, plateau);

            String commands = sc.next();
            processor.process(rover, commands);

            System.out.println(rover.getPosition());
        }

        sc.close();
    }
}
