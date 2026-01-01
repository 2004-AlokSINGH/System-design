A Quick Note on Cloning:

shallow copy - means share same refenrce by both object

copy primitive value- means copy just value in new memory area not the refernce so change in new copied object not change older.



If both the original and the clone hold a reference to the same List, any mutation through one reference will be visible through the other. This leads to several problems:

- Unexpected shared mutations: adding/removing or mutating elements via the clone also changes the original (and vice‑versa), causing surprising behavior and bugs.
- Broken invariants/encapsulation: callers that expect an independent copy can unintentionally observe or break internal state.
- Concurrency hazards: simultaneous access can cause race conditions.
- Iteration errors: modifying the list while another object is iterating it can throw ConcurrentModificationException (Java).

Mitigations:
- Perform a deep copy of mutable fields (copy the list and its mutable elements).
- Use defensive copies in constructors and getters/setters.
- Return unmodifiable views or use immutable collections when possible.
- For complex object graphs, keep a map original->copy to preserve shared structure and avoid infinite recursion.

Deep Copy: If your object contains mutable reference types, you should create a deep copy in the copy constructor.
Deep copy -
-  Top-level object and all reachable mutable nested objects are duplicated.
-  Each copy has its own separate instances (different memory).
-  Changes to nested objects do not affect the other copy.

Concrete example — Shallow vs Deep copy (Java)

Shallow copy: the collection reference (and/or mutable elements) is shared between original and clone.
Deep copy: a new collection is created and each mutable element is also copied.

Example code:

```java
// Simple mutable element with its own copy constructor
public class Item {
    private String name;
    private int durability;

    public Item(String name, int durability) {
        this.name = name;
        this.durability = durability;
    }

    // copy constructor
    public Item(Item other) {
        this.name = other.name;
        this.durability = other.durability;
    }

    public void damage(int d) { this.durability -= d; }

    @Override
    public String toString() {
        return name + "(" + durability + ")";
    }
}

public class Enemy {
    private String type;
    private int health;
    private List<Item> inventory; // mutable field

    public Enemy(String type, int health, List<Item> inventory) {
        this.type = type;
        this.health = health;
        this.inventory = inventory;
    }

    // Shallow copy constructor: inventory reference is shared
    public Enemy(Enemy other) {
        this.type = other.type;
        this.health = other.health;
        this.inventory = other.inventory; // <-- shared list
    }

    // Deep-copy factory / constructor: new list and deep-copy each Item
    public Enemy deepCopy() {
        List<Item> invCopy = null;
        if (this.inventory != null) {
            invCopy = new ArrayList<>(this.inventory.size());
            for (Item it : this.inventory) {
                invCopy.add(new Item(it)); // Item copy constructor
            }
        }
        return new Enemy(this.type, this.health, invCopy);
    }

    @Override
    public String toString() {
        return type + " hp=" + health + " inv=" + inventory;
    }
}

// Demo showing difference
public class Demo {
    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Sword", 10));

        Enemy original = new Enemy("Flying", 100, items);
        Enemy shallowClone = new Enemy(original);     // shares inventory
        Enemy deepClone = original.deepCopy();        // fully copied inventory

        // mutate shallow clone's inventory item and add a new item
        shallowClone.inventory.get(0).damage(2);
        shallowClone.inventory.add(new Item("Shield", 5));

        // Observations:
        System.out.println("original: " + original);    // shows Sword(8) and Shield added (affected)
        System.out.println("shallow: " + shallowClone); // same as original
        System.out.println("deep: " + deepClone);       // unaffected: Sword(10)
    }
}
```

Notes:
- For true deep copy you must provide a deep-copy mechanism for every mutable referenced object.
- For object graphs with cycles, maintain a map of original->copy while copying to avoid infinite recursion and preserve shared references.
- Use defensive copying on public getters/setters to avoid exposing internal mutable state.
  