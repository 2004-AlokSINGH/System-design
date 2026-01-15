package marsrover.example1marsrover;


public class Pleateau {

// Use final for object properties that should never change after construction.
// You can still have multiple objects with different values for those properties.

    private final int maxX;
    private final int maxY;

    // other variation
    // 1.private final int maxX;  Immutable
    //      Once set in the constructor, never changes.
    //      Cons: Can’t resize or reconfigure after creation.
    
    // 2. private int maxX;   Mutable
    //            Meaning: Can be changed later with setters.
    //            Use case: When the object’s state is expected to evolve (e.g., a resizable plateau).
    
    // 3. public static final int DEFAULT_MAX_X = 5;
    //     Shared constant values across all objects.
    //     Use case: Default configurations, universal constants (like Earth’s gravity).



    public Pleateau(int maxX, int maxY) { 
        this.maxX = maxX; this.maxY = maxY;
    }

    // if no getter and setter - Meaning: Object is fully immutable.
    public boolean isWithinBounds(int x, int y) { 
        return x >= 0 && y >= 0 && x <= maxX && y <= maxY;
    }
    
}






// Builder Pattern (for complex immutability) :--

// public class Plateau {
//     private final int maxX;
//     private final int maxY;

//     private Plateau(Builder builder) {
//         this.maxX = builder.maxX;
//         this.maxY = builder.maxY;
//     }

//     public static class Builder {
//         private int maxX;
//         private int maxY;

//         public Builder maxX(int maxX) { this.maxX = maxX; return this; }
//         public Builder maxY(int maxY) { this.maxY = maxY; return this; }
//         public Plateau build() { return new Plateau(this); }
//     }
// }




// other way of defining class

// 2. Static Nested Class
// public class Outer {
//     static class Nested {
//         void display() { System.out.println("Static Nested Class"); }
//     }
// }
// A static nested class can directly access all static members (variables and methods) of the outer class, including those marked as private. It cannot directly
//  access non-static (instance) members because it is not associated with a specific instance of the outer class.




// Inner Class (Non-Static Nested Class)
// java
// public class Outer {
//     class Inner {
//         void show() { System.out.println("Inner Class"); }
//     }
// }
// Declared inside another class without static.

// Requires an instance of the outer class to be created:

// java
// Outer.Inner inner = new Outer().new Inner();
// Useful when the inner class logically belongs to the outer 
// class and needs access to its members.




// Anonymous Class
// java
// Runnable r = new Runnable() {
//     @Override
//     public void run() {
//         System.out.println("Anonymous Class");
//     }
// };
// No name, declared and instantiated in one go.

// Often used for quick implementations of interfaces or abstract classes.

// Common in event handling or callbacks.



// Interface (Functional Variation)

// public interface Movable {
//     void move();
// }
// Defines a contract (methods) without implementation.

// Classes implement it.

// Since Java 8, can have default and static method