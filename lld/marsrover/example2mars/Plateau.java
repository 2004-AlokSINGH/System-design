package marsrover.example2mars;

// Represents the Mars plateau boundaries
// Public because it is used across multiple layers
public class Plateau {

    // Private to prevent unauthorized boundary changes
    private final int maxX;
    private final int maxY;

    public Plateau(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    // Public validation method — behavior, not data exposure
    public boolean isWithinBounds(Position position) {
        return position.getX() >= 0 &&
               position.getY() >= 0 &&
               position.getX() <= maxX &&
               position.getY() <= maxY;
    }
}
