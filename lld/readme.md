# Design Pattern Implementation Guide

A comprehensive reference for implementing design patterns in Java, organized by type with detailed step-by-step instructions, examples, and cross-references to pattern documentation.

---

## Table of Contents
1. [Creational Patterns](#creational-patterns) — Object creation mechanisms
2. [Behavioral Patterns](#behavioral-patterns) — Object collaboration & responsibility distribution
3. [Structural Patterns](#structural-patterns) — Object composition & relationships
4. [General Implementation Principles](#general-implementation-principles)

---

# CREATIONAL PATTERNS
Patterns that deal with object creation mechanisms, trying to create objects in a manner suitable to the situation.

## 1. Builder Pattern
**Purpose:** Construct complex objects step-by-step, separating construction logic from representation. Ideal for objects with many optional fields.

**When to Use:**
- Objects require many optional parameters
- You want to avoid telescoping constructors or setter-based mutation
- You need immutable objects with complex initialization

**Implementation Steps:**

1. **Create the product class first** (e.g., `HttpReq`, `HttpReq2`)
   - Define all fields (required and optional)
   - Make the class immutable (private fields, final class if possible)
   - Reason: Defines the shape and constraints the builder must populate

2. **Create the Builder interface/class** (e.g., `HttpReqBuilder`, nested `Builder`)
   - Can be an interface with multiple implementations or a single builder class
   - Keep all builder fields mutable for flexible construction

3. **Implement fluent setter methods**
   - Each method returns `this` for method chaining
   - Example: `builder.setUrl("http://...").setMethod("POST").setBody(...)`
   - Reason: Provides readable, chainable API

4. **Implement the `build()` method**
   - Validate invariants and set defaults
   - Return an immutable instance of the product

5. **Add convenience factory methods** (optional)
   - Example: `HttpReq.builder()` returns a new builder instance
   - Simplifies client usage

**Related Files:**
- [creational/builder/note.md](creational/builder/note.md) — Detailed explanation with telescoping constructor examples
- [creational/builder/Builder.java](creational/builder/Builder.java) — Interface
- [creational/builder/HttpReq.java](creational/builder/HttpReq.java) — Simple product
- [creational/builder/HttpReq2.java](creational/builder/HttpReq2.java) — Complex product with nested builder
- [creational/builder/HttpReqBuilder.java](creational/builder/HttpReqBuilder.java) — Standalone builder

**Key Insight:** Separating construction from representation allows flexible, readable object creation while maintaining immutability.

---

## 2. Factory Pattern
**Purpose:** Encapsulate object creation to decouple clients from concrete classes.

**When to Use:**
- Creation logic is complex or varies by type
- You want to centralize and control instantiation
- Families of related objects need to be created together

**Three Main Approaches:**

### Option A: Simple Factory
Centralized factory method that creates objects based on a type parameter.

```java
class NotificationFactory {
    public static Notification create(String type) {
        switch(type) {
            case "email": return new EmailNotification();
            case "sms": return new SMSNotification();
            default: throw new IllegalArgumentException("Unknown type");
        }
    }
}
```

**Pros:** Simple, centralized logic  
**Cons:** Violates Open/Closed Principle (must modify factory for new types)

### Option B: Factory Method (Preferred)
Abstract creator class with abstract creation method; concrete creators implement it.

```java
abstract class NotificationCreator {
    public abstract Notification createNotification();
    // ... other template logic
}

class EmailNotificationCreator extends NotificationCreator {
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
```

**Pros:** Open/Closed Principle; extensible without modifying existing code  
**Cons:** Requires class hierarchy for each product type

### Option C: Abstract Factory
Creates families of related objects through multiple factory methods.

**Implementation Steps:**

1. **Define the product interface first** (e.g., `Notification`)
   - Declare the contract clients depend on
   - Enables multiple interchangeable implementations

2. **Implement concrete products** (e.g., `EmailNotification`, `SMSNotification`)
   - Encapsulate specific creation details and behavior

3. **Create the factory layer**
   - Simple Factory: static methods
   - Factory Method: abstract class with subclasses
   - Abstract Factory: factory interface with multiple creation methods

4. **Use dependency injection**
   - Client receives factory via constructor, not hardcoded
   - Improves testability

**Related Files:**
- [behavioral/strategy/README.md](behavioral/strategy/README.md) — Similar approach (context pattern)
- [creational/factory/Notification.java](creational/factory/Notification.java) — Product interface
- [creational/factory/EmailNotification.java](creational/factory/EmailNotification.java) — Concrete product
- [creational/factory/NotificationCreatorFactoryMethod.java](creational/factory/NotificationCreatorFactoryMethod.java) — Factory method approach
- [creational/factory/NotificationSimpleFactory.java](creational/factory/NotificationSimpleFactory.java) — Simple factory approach

**Key Insight:** Defer instantiation to subclasses or factory methods, keeping clients independent of concrete types.

---

## 3. Prototype Pattern
**Purpose:** Create new objects by cloning existing ones instead of instantiation from scratch.

**When to Use:**
- Object creation is expensive or complex
- You need many similar objects with slight variations
- You want to avoid repeating initialization logic
- The object doesn't know its own concrete class (polymorphism)

**The Problem:**
- Manual field-by-field copying violates encapsulation
- Tightly couples code to concrete classes
- You may not know the object's actual class at compile time

**The Solution:**
- Object exposes a `clone()` or `copy()` method
- Clients request clones instead of creating new instances
- Reduces initialization overhead and code duplication

**Implementation Steps:**

1. **Define the Prototype interface** (e.g., `EnemyPrototype`)
   - Declare a `clone()` or `copy()` method

2. **Implement the concrete prototype class** (e.g., `Enemy`)
   - **Important:** Handle shallow vs. deep copy correctly
   - Shallow copy: Fast but shares mutable references
   - Deep copy: Slower but fully independent clones
   - See [creational/prototype/copyNote.md](creational/prototype/copyNote.md) for detailed explanation

3. **Create a Prototype Registry** (optional, e.g., `EnemyRegistry`)
   - Store pre-configured prototypes
   - Clients clone prototypes instead of creating from scratch
   - Central place to manage prototype configurations

4. **Use in client code**
   ```java
   Enemy originalEnemy = new Enemy("Flying", 100, /* ... */);
   Enemy clonedEnemy = originalEnemy.clone();  // Instead of re-instantiating
   ```

**Shallow vs. Deep Copy Decision:**
- **Shallow copy** (simpler, use when):
  - All fields are primitives or immutable (e.g., String, Integer)
  - Fields don't need independent modification
- **Deep copy** (safer, use when):
  - Object contains mutable reference types (List, custom objects)
  - Cloned object must be fully independent
  - Modifications to one clone shouldn't affect others

**Related Files:**
- [creational/prototype/copyNote.md](creational/prototype/copyNote.md) — Detailed analysis of shallow vs. deep copy
- [creational/prototype/Enemy.java](creational/prototype/Enemy.java) — Concrete prototype
- [creational/prototype/EnemyPrototype.java](creational/prototype/EnemyPrototype.java) — Prototype interface
- [creational/prototype/EnemyRegistry.java](creational/prototype/EnemyRegistry.java) — Prototype storage

**Key Insight:** Let objects clone themselves; avoids coupling to concrete classes and expensive re-initialization.

---

## 4. Singleton Pattern
**Purpose:** Ensure a class has exactly one instance and provide global access to it.

**When to Use:**
- Resource must be shared across the application (database connection, logger)
- Only one instance makes sense (config manager, thread pool)
- Global state must be controlled and consistent

**Common Pitfalls:**
- Thread safety issues in multi-threaded environments
- Difficulty testing (hard to mock a singleton)
- Hidden dependencies (global access makes code harder to follow)

**Implementation Approaches:**

### Approach 1: Eager Initialization (Simple)
```java
public class Singleton {
    public static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { }
}
```
- Instance created at class loading time
- Thread-safe (JVM guarantees class initialization is thread-safe)
- Not lazy (wastes resources if never used)

### Approach 2: Lazy Initialization (Lazy Singleton)
```java
public class LazySingleton {
    private static LazySingleton instance;
    
    private LazySingleton() { }
    
    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```
- Instance created on first use
- Thread-safe via synchronization
- Synchronization overhead on every call

### Approach 3: Double-Checked Locking (Best for Java)
```java
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;
    
    private DoubleCheckedSingleton() { }
    
    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {  // First check: fast path
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {  // Second check: ensure safety
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}
```
- Lazy initialization with minimal synchronization overhead
- First check avoids synchronization after initialization
- `volatile` keyword prevents instruction reordering
- Recommended for high-performance scenarios

### Approach 4: Bill Pugh Singleton (Cleanest)
```java
public class BillPughSingleton {
    private BillPughSingleton() { }
    
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
    
    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```
- Uses class loader initialization mechanism
- Thread-safe by design
- Clean, concise syntax
- Preferred in modern Java

**Related Files:**
- [creational/singleton/note.md](creational/singleton/note.md) — Overview and basic concepts
- [creational/singleton/LazySingleton.java](creational/singleton/LazySingleton.java) — Synchronized approach
- [creational/singleton/DoubleCheckedSingleton.java](creational/singleton/DoubleCheckedSingleton.java) — Optimized approach
- [creational/singleton/ThreadSafeSingleton.java](creational/singleton/ThreadSafeSingleton.java) — Thread-safe variant

**Key Insight:** Control instance creation strictly; balance between thread safety, lazy loading, and performance overhead.

---

# BEHAVIORAL PATTERNS
Patterns that define how objects interact and distribute responsibility.

## 5. Strategy Pattern
**Purpose:** Define a family of algorithms, encapsulate each one, and make them interchangeable.

**When to Use:**
- Multiple ways to accomplish a task
- Algorithm selection varies at runtime based on context
- You want to avoid conditional logic (if/switch statements)
- New algorithms should be added without modifying existing code

**The Problem (Without Strategy):**
```java
public void payment(String method, int amount) {
    if ("upi".equals(method)) {
        // UPI payment logic
    } else if ("card".equals(method)) {
        // Card payment logic
    } else if ("wallet".equals(method)) {
        // Wallet payment logic
    }
    // More conditions as new payment methods are added...
}
```
- Violates Single Responsibility (mixing multiple algorithms)
- Violates Open/Closed Principle (must modify method for new types)
- Hard to test individual strategies
- Code becomes unmaintainable as strategies grow

**Implementation Steps:**

1. **Define the strategy interface first** (e.g., `PaymentStrategy`)
   - Declares the contract all algorithms implement
   - Example: `void paymentMethod(int amount)`

2. **Implement concrete strategies** (e.g., `CardPaymentStrategy`, `UpiPaymentStrategy`)
   - Each encapsulates one algorithm variant
   - No knowledge of other strategies

3. **Create the context class** (e.g., `PaymentSystem`)
   - Receives strategy via constructor or setter (dependency injection)
   - Delegates to strategy when needed: `strategy.paymentMethod(amount)`
   - Client code becomes: `paymentSystem.performOperation(amount)`

4. **Optionally add a factory/registry**
   - Centralize strategy selection logic
   - Example: `PaymentFactory.getStrategy(methodType)`

**Related Files:**
- [behavioral/strategy/PaymentStrategy.java](behavioral/strategy/PaymentStrategy.java) — Interface
- [behavioral/strategy/UpiPaymentStrategy.java](behavioral/strategy/UpiPaymentStrategy.java) — Concrete strategy
- [behavioral/strategy/CardPaymentStrategy.java](behavioral/strategy/CardPaymentStrategy.java) — Concrete strategy
- [behavioral/strategy/PaymentSystem.java](behavioral/strategy/PaymentSystem.java) — Context class
- [behavioral/strategy/PaymentClient.java](behavioral/strategy/PaymentClient.java) — Client usage
- [behavioral/strategy/README.md](behavioral/strategy/README.md) — Detailed example

**Benefits:**
- Eliminates conditional logic
- Easy to add new strategies
- Strategies are testable in isolation
- Runtime algorithm selection

**Key Insight:** Encapsulate each algorithm as an object; let clients choose at runtime without modifying existing code.

---

## 6. Template Method Pattern
**Purpose:** Define the skeleton of an algorithm in a base class, letting subclasses override specific steps.

**When to Use:**
- You have a fixed sequence of steps with some variable steps
- Multiple classes share similar algorithm structure but differ in details
- You want to enforce a specific order of operations
- Common in frameworks and pipelines (ML training, CI/CD, data processing)

**The Problem (Without Template Method):**
- Each subclass implements the entire algorithm, leading to code duplication
- Risk of wrong step ordering or missing steps
- Hard to enforce consistent behavior across implementations

**The Solution:**
- Base class provides the immutable template (fixed order)
- Subclasses override only the variable steps
- Clients always call the template method

**Implementation Steps:**

1. **Create the abstract template class** (e.g., `ModelTrainer`)
   - Define a **final** template method that defines algorithm skeleton
   - Example: `public final void executionTemplate()`
   - Reason: Prevents subclasses from overriding the overall structure

2. **Define abstract methods for variable steps** (e.g., `loadData()`, `preprocessData()`, `trainData()`)
   - Marked as `abstract` — subclasses must implement
   - Called in the fixed sequence by the template method

3. **Optionally provide hook methods**
   - Non-abstract methods with default behavior
   - Subclasses can override for customization
   - Example: `public void validate()` with empty default

4. **Implement concrete subclasses** (e.g., `MLModel`)
   - Override abstract methods with specific implementations
   - Follow the fixed sequence defined by template

5. **Client code**
   ```java
   ModelTrainer trainer = new MLModel();
   trainer.executionTemplate();  // Fixed sequence guaranteed
   ```

**Template vs. Concrete Methods:**
```java
public abstract class ModelTrainer {
    // Template method: final, defines fixed sequence
    public final void executionTemplate() {
        loadData();          // Abstract - must implement
        preprocessData();    // Abstract - must implement
        trainData();         // Abstract - must implement
        evaluateModel();     // Abstract - must implement
        saveModel("default");// Concrete - inherited
    }
    
    protected abstract String loadData();
    protected abstract String preprocessData();
    protected abstract String trainData();
    protected abstract String evaluateModel();
    protected String saveModel(String path) { /* default */ }
}
```

**Related Files:**
- [behavioral/template/note.md](behavioral/template/note.md) — Detailed explanation with UML diagram
- [behavioral/template/ModelTrainer.java](behavioral/template/ModelTrainer.java) — Abstract template class
- [behavioral/template/MLModel.java](behavioral/template/MLModel.java) — Concrete implementation
- [behavioral/template/Client.java](behavioral/template/Client.java) — Client usage

**Benefits:**
- Enforces algorithm structure
- Reduces code duplication
- Improves maintainability and consistency
- Subclasses focus only on their variant steps

**Key Insight:** Fix the algorithm structure at the base level; let subclasses fill in the details.

---

## 7. Iterator Pattern
**Purpose:** Provide sequential access to collection elements without exposing its internal structure.

**When to Use:**
- Collections need multiple traversal methods (forward, reverse, filter, shuffle)
- Internal representation might change (ArrayList to LinkedList)
- You want to decouple client code from collection internals
- Support different iteration styles on the same collection

**The Problem (Without Iterator):**
```java
class Playlist {
    private List<String> songs = new ArrayList<>();
    public List<String> getSongs() { return songs; }  // Exposes internals!
}

// Client code tightly coupled to List implementation
for (String song : playlist.getSongs()) {
    // If Playlist switches to LinkedList, client breaks
    // Can't easily add shuffle, reverse, or filter traversal
}
```
- Breaks encapsulation (exposes internal collection)
- Client couples to implementation type (ArrayList, LinkedList, etc.)
- Hard to support multiple traversal strategies

**The Solution:**
- Collection provides an iterator object
- Iterator encapsulates traversal logic
- Client uses common interface: `hasNext()` and `next()`

**Implementation Steps:**

1. **Define iterator interfaces** (e.g., `Iteratorr<T>`, `IterableCollection<T>`)
   - `Iteratorr<T>`: `hasNext(): boolean`, `next(): T`
   - `IterableCollection<T>`: `createIterator(): Iteratorr<T>`

2. **Implement the concrete collection** (e.g., `ConcretePlaylist`)
   - Implements `IterableCollection<T>`
   - Keeps internal storage (List, Array, Tree) private
   - Provides `createIterator()` method

3. **Implement the iterator class** (e.g., `PlayListIterator`)
   - Implements `Iteratorr<T>`
   - Holds reference to collection and traversal state (index, cursor)
   - Implements `hasNext()` and `next()`

4. **Support multiple iterator variants**
   - `ForwardIterator`: traverse left-to-right
   - `ReverseIterator`: traverse right-to-left
   - `ShuffleIterator`: random order
   - `FilterIterator`: skip certain elements

5. **Client code**
   ```java
   Iteratorr<String> iter = playlist.createIterator();
   while (iter.hasNext()) {
       System.out.println(iter.next());
   }
   ```

**Related Files:**
- [behavioral/iterator/note.md](behavioral/iterator/note.md) — Comprehensive explanation with examples
- [behavioral/iterator/Iteratorr.java](behavioral/iterator/Iteratorr.java) — Iterator interface
- [behavioral/iterator/IterableCollection.java](behavioral/iterator/IterableCollection.java) — Collection interface
- [behavioral/iterator/ConretePlaylist.java](behavioral/iterator/ConretePlaylist.java) — Concrete collection
- [behavioral/iterator/PlayListItertor.java](behavioral/iterator/PlayListItertor.java) — Concrete iterator
- [behavioral/iterator/MusicPlayer.java](behavioral/iterator/MusicPlayer.java) — Client usage

**Benefits:**
- Hides collection internals
- Supports multiple traversal strategies
- Easy to change internal representation
- Clients remain decoupled from implementation

**Key Insight:** Abstract iteration into an iterator object; let collections provide different iterator implementations for different traversal needs.

---

# STRUCTURAL PATTERNS
Patterns that define how objects are combined and related to form larger structures.

## 8. Adapter Pattern
**Purpose:** Convert the interface of a class into another interface clients expect, allowing incompatible classes to work together.

**When to Use:**
- Integrating third-party libraries or legacy code
- Two incompatible interfaces need to work together
- You want to reuse a class with an incompatible interface
- Creating a wrapper over external APIs

**The Problem:**
- Existing code expects interface `IReport.getJSONData()`
- Third-party provider offers `XMLDataProvider.getXMLData()`
- You can't directly use third-party because of interface mismatch
- Changing existing code is risky or not allowed

**The Solution:**
- Create an adapter class that implements the expected interface
- Adapter internally uses the third-party class
- Clients use the adapter, unaware of the underlying difference

**Two Implementation Styles:**

### Object Adapter (Composition - Preferred in Java)
```java
public class XMLDataProviderAdapter implements IReport {
    private XMLDataProvider xmlProvider;  // Composition
    
    public XMLDataProviderAdapter(XMLDataProvider provider) {
        this.xmlProvider = provider;
    }
    
    @Override
    public String getJSONData() {
        String xmlData = xmlProvider.getXMLData();
        return convertXMLToJSON(xmlData);  // Adapt interface
    }
    
    private String convertXMLToJSON(String xml) {
        // Conversion logic
    }
}
```
- Flexible (can wrap any object)
- Doesn't require multiple inheritance
- **Preferred in Java**

### Class Adapter (Inheritance - Rare in Java)
```java
public class XMLDataProviderAdapter extends XMLDataProvider implements IReport {
    @Override
    public String getJSONData() {
        String xmlData = super.getXMLData();
        return convertXMLToJSON(xmlData);
    }
}
```
- More direct but requires multiple inheritance
- Not possible in Java (single inheritance only)
- Useful in C++

**Implementation Steps:**

1. **Identify the target interface** (e.g., `IReport`, `PaymentProcessor`)
   - What interface does the client expect?

2. **Identify the adaptee/incompatible interface** (e.g., `XMLDataProvider`)
   - The existing class that needs adaptation

3. **Create the adapter class**
   - Implements the target interface
   - Uses composition: holds a reference to adaptee
   - Translates method calls from target to adaptee

4. **Use in client code**
   ```java
   XMLDataProvider xmlProvider = new XMLDataProvider();
   IReport adapter = new XMLDataProviderAdapter(xmlProvider);
   String jsonData = adapter.getJSONData();
   ```

**Related Files:**
- [structural/adapter/note.md](structural/adapter/note.md) — Detailed explanation
- [structural/adapter/PaymentProcessor.java](structural/adapter/PaymentProcessor.java) — Target interface example

**Benefits:**
- Reuse incompatible classes without modification
- Decouple client from third-party API changes
- Single Responsibility Principle (adapter handles translation)

**Key Insight:** Create a bridge between incompatible interfaces using composition; clients use the adapter transparently.

---

## 9. Proxy Pattern
**Purpose:** Provide a surrogate or placeholder for another object to control access to it.

**When to Use:**
- Lazy loading (defer expensive initialization until needed)
- Access control (restrict who can use the object)
- Logging or auditing (track access patterns)
- Caching (avoid repeated expensive operations)
- Remote access (hide remote communication complexity)

**The Problem (Without Proxy):**
```java
Image image = new HighResolutionImage("photo.jpg");  // Loads immediately (2 seconds)
// ... much later in code ...
image.display();  // Finally use it
```
- Image loads even if never used (waste of resources)
- No logging of access
- No access control
- No caching

**The Solution:**
- Create a proxy that implements the same interface
- Proxy defers or controls access to the real object
- Client uses proxy, unaware of the difference

**Implementation Steps:**

1. **Define the subject interface** (e.g., `Image`)
   - Both proxy and real object implement this
   - Example: `void display()`, `String getFileName()`

2. **Implement the real subject** (e.g., `HighResolutionImage`)
   - The actual, potentially expensive object
   - Constructor loads data from disk (expensive)
   - `display()` shows the image

3. **Implement the proxy** (e.g., `ImageProxy`)
   - Implements same `Image` interface
   - Holds a lazy reference to `HighResolutionImage`
   - Creates real object only on first use (`ensureLoaded()`)
   - Can add logging, access control, or caching

4. **Client code** (unchanged)
   ```java
   Image image = new ImageProxy("photo.jpg");  // Fast! No load yet
   image.display();  // Lazy load happens here
   ```

**Proxy Types:**

| Type | Purpose | Example |
|------|---------|---------|
| **Virtual Proxy** | Lazy initialization | `ImageProxy` — load on first use |
| **Protection Proxy** | Access control | Check permissions before allowing access |
| **Remote Proxy** | Hide remote communication | Communicate with server transparently |
| **Caching Proxy** | Cache results | Store expensive computation results |
| **Smart Proxy** | Add behavior | Logging, reference counting, cleanup |

**Related Files:**
- [structural/proxy/note.md](structural/proxy/note.md) — Detailed explanation with examples
- [structural/proxy/Image.java](structural/proxy/Image.java) — Subject interface
- [structural/proxy/HighResolutionImage.java](structural/proxy/HighResolutionImage.java) — Real subject
- [structural/proxy/ImageProxy.java](structural/proxy/ImageProxy.java) — Proxy implementation
- [structural/proxy/Client.java](structural/proxy/Client.java) — Client usage

**Benefits:**
- Defers expensive operations
- Adds access control/logging without modifying real object
- Client remains unaware of proxy presence
- Reduces resource usage (lazy loading)

**Key Insight:** Intercept access to an object through a proxy; add behavior (logging, caching, access control) without modifying the real object.

---

## 10. Composite Pattern
**Purpose:** Compose objects into tree structures to represent part-whole hierarchies, allowing clients to treat individual objects and compositions uniformly.

**When to Use:**
- Hierarchical structures (file systems, organizational charts, UI components)
- Clients need to treat leaves and containers the same
- You want to simplify client code by providing uniform interface
- Operations should work recursively on the entire tree

**The Problem (Without Composite):**
```java
File file = new File("document.txt");
Folder folder = new Folder("Documents");

// Client must treat File and Folder differently
if (file instanceof File) {
    System.out.println(file.getSize());
} else if (file instanceof Folder) {
    System.out.println(folder.getTotalSize());  // Different method!
}
```
- Violates Open/Closed Principle
- Client code is messy with type checks
- Hard to work uniformly across the tree

**The Solution:**
- Define common interface `FileSystemItem`
- Both `File` (leaf) and `Folder` (composite) implement it
- `Folder` maintains a collection of `FileSystemItem`
- Client uses uniform interface for both

**Implementation Steps:**

1. **Define the component interface** (e.g., `FileSystemItem`)
   - Methods that apply to both leaves and composites
   - Examples: `getSize()`, `getName()`, `open()`, `print()`

2. **Implement leaf classes** (e.g., `File`)
   - Implements `FileSystemItem`
   - Performs operations directly
   - No children

3. **Implement composite classes** (e.g., `Folder`)
   - Implements `FileSystemItem`
   - Maintains `List<FileSystemItem>` children
   - Implements operations by delegating/aggregating to children
   - Provides `add(FileSystemItem)` and `remove(FileSystemItem)`

4. **Operations apply uniformly**
   ```java
   FileSystemItem item = /* File or Folder */;
   int size = item.getSize();  // Works for both!
   ```

**Example Structure:**
```
FileSystemItem (interface)
├── File (leaf)
│   └── getSize() - returns file size
│   └── getName() - returns file name
└── Folder (composite)
    ├── getSize() - sums children sizes
    ├── getName() - returns folder name
    └── children: List<FileSystemItem>
```

**Recursive Operations:**
```java
public int getSize() {  // In Folder class
    int total = 0;
    for (FileSystemItem item : children) {
        total += item.getSize();  // Works for File or Folder!
    }
    return total;
}
```

**Related Files:**
- [structural/composite/note.md](structural/composite/note.md) — Brief explanation
- [structural/composite/FileSystemItem.java](structural/composite/FileSystemItem.java) — Component interface
- [structural/composite/File.java](structural/composite/File.java) — Leaf class
- [structural/composite/Folder.java](structural/composite/Folder.java) — Composite class
- [structural/composite/Client.java](structural/composite/Client.java) — Client usage

**Benefits:**
- Uniform treatment of leaves and composites
- Client code is simpler and cleaner
- Easy to add new types (extend interface, implement)
- Recursive operations naturally work through the tree

**Key Insight:** Create a common interface for leaves and composites; clients use it uniformly without knowing the internal structure.

---

# GENERAL IMPLEMENTATION PRINCIPLES

Apply these principles to all pattern implementations:

## 1. **Start with Abstractions (Interfaces/Abstract Classes)**
- Define the interface/abstract contract first
- Clients depend on the abstraction, not concrete classes
- Maximizes decoupling and enables testing/mocking
- Example: Start with `PaymentStrategy`, then implement `CardPaymentStrategy`

## 2. **Composition Over Inheritance**
- Prefer composition (object contains another) over inheritance
- More flexible, avoids tight coupling
- Better supports multiple behavior combinations
- Example: Adapter uses composition (`holds XMLDataProvider`), not inheritance

## 3. **Single Responsibility Principle**
- Each class has one reason to change
- Builder handles construction, not the product
- Iterator handles traversal, not the collection
- Proxy controls access, not the real subject

## 4. **Dependency Injection**
- Pass dependencies via constructor or setter, not hardcoded
- Improves testability (can inject mocks)
- Makes relationships explicit
- Example: `PaymentSystem(PaymentStrategy strategy)`

## 5. **Keep File Organization Consistent**
```
pattern-name/
├── [Interface].java         — Contract
├── [Concrete1].java         — Implementation 1
├── [Concrete2].java         — Implementation 2
├── [Helper/Factory].java    — Helper classes (if needed)
├── Client.java              — Usage example
└── note.md                  — Documentation
```

## 6. **Document with Examples**
- Provide working client examples in `Client.java`
- Include design rationale in `note.md`
- Show before/after comparisons (problem → solution)
- Add comments for non-obvious logic

## 7. **Test Edge Cases**
- Test normal flows
- Test boundary conditions
- Test error handling
- Test performance (for patterns affecting performance like Proxy, Singleton)

## 8. **Cross-Pattern References**
- Link related patterns
- Show when one pattern complements another
- Example: Factory + Strategy, Builder + Composite

---

## Quick Reference: When to Use Each Pattern

| Pattern | Problem | Solution |
|---------|---------|----------|
| **Builder** | Complex object, many optional fields | Step-by-step construction with fluent API |
| **Factory** | Tightly coupled to concrete classes | Centralized object creation |
| **Prototype** | Expensive object creation | Clone existing objects |
| **Singleton** | Multiple instances cause issues | Ensure one instance only |
| **Strategy** | Hard-coded algorithm selection | Encapsulate algorithms as interchangeable objects |
| **Template Method** | Code duplication in similar algorithms | Fix skeleton, vary only specific steps |
| **Iterator** | Exposing internal collection structure | Provide uniform traversal without exposing internals |
| **Adapter** | Incompatible interfaces | Create bridge using composition |
| **Proxy** | Need to control access or defer initialization | Surrogate object controlling real object |
| **Composite** | Part-whole hierarchies | Uniform interface for leaves and containers |

---

*This guide is structured to provide both high-level understanding and practical implementation steps. Refer to the linked note.md files for deeper conceptual explanations and pattern variations.*
