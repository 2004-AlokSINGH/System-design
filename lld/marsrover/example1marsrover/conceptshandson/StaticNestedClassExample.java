package marsrover.example1marsrover.conceptshandson;

public class StaticNestedClassExample {
    private static String staticMsg = "Static Member Access";
    private String instanceMsg = "Instance Member Access";

    // Static Nested Class
    static class Nested {
        void display( StaticNestedClassExample outerInstance) {
            // 1. Direct access to static members (even private)
            System.out.println(staticMsg); 

            // 2. Direct access to instance members fails
            // System.out.println(instanceMsg); // COMPILE ERROR

            // 3. Indirect access to instance members via reference
            System.out.println(outerInstance.instanceMsg); 
        }
    }

    public static void main(String[] args) {
        // Instantiate the nested class without an Outer instance
        StaticNestedClassExample.Nested nestedObj = new StaticNestedClassExample.Nested();
        
        // Create an Outer instance to pass for instance member access
        StaticNestedClassExample outerObj = new StaticNestedClassExample();
        nestedObj.display(outerObj);
    }
}
