package creational.prototype;

import java.util.HashMap;
import java.util.Map;

// Step 3 (Optional): Create a Prototype Registry (EnemyRegistry)
// A Prototype Registry (or Manager) stores pre-configured prototype instances.
// This keeps your code organized, especially when you have many types of enemies.

public class EnemyRegistry {

    private Map<String, Enemy> prototypes=new HashMap<>();

    public void register(String key, Enemy prototype){
        prototypes.put(key, prototype);
    }
     public Enemy get(String key) {
        Enemy prototype = prototypes.get(key);
        if (prototype != null) {
            return prototype.clone();
        }
        throw new IllegalArgumentException("No prototype registered for: " + key);
    }
    
    
}
