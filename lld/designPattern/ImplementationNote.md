# Implementation Notes (concise) — what to create first, why, and how to implement

## Builder
1. Create the product class first (e.g., `HttpReq` / `HttpReq2`) — reason: defines immutable/complex object shape and required fields that the builder will populate.  
2. Create the builder interface/class (`HttpReqBuilder` or nested `Builder`) — reason: encapsulates construction steps and fluent API.  
3. Implement fluent setter methods on the builder, keep fields mutable there, implement `build()` to validate defaults/invariants and return the product.  
4. Add convenience factory methods (e.g., `HttpReq2.builder()`), unit tests, and example usage: `new HttpReqBuilder().setX(...).build()`.  
Files: `HttpReq.java`, `HttpReqBuilder.java` (or nested inside `HttpReq2`).

## Strategy
1. Define the strategy interface first (e.g., `PaymentStrategy`) — reason: declares the contract callers depend on and enables multiple interchangeable implementations.  
2. Implement concrete strategies next (`CardPaymentStrategy`, `UpiPaymentStrategy`) — reason: encapsulate algorithm variants.  
3. Create the client/consumer (`PaymentClient` / `PaymentService`) that receives a `PaymentStrategy` via constructor or setter (dependency injection) and calls the interface method.  
4. Optionally add a small factory/registry to pick a strategy at runtime by key/config.  
Files: `PaymentStrategy.java`, `CardPaymentStrategy.java`, `PaymentClient.java`.

## Template
1. Create the abstract template class first (`ModelTrainer`) with a final template method (e.g., `train()`) that defines the algorithm skeleton. — reason: fixes invariant steps and ordering.  
2. Define protected abstract/hook methods for overridable steps (`setup()`, `trainEpoch()`, `validate()`, `teardown()`) with reasonable defaults/no-ops where appropriate.  
3. Implement concrete subclasses (`ConcreteModelTrainer` / `MLModel` implementations) that override the variable steps.  
4. Client constructs the concrete trainer and calls the final `train()` method.  
Files: `ModelTrainer.java`, `MyModelTrainer.java`.

## Proxy
1. Define the subject interface first (e.g., `Image`) — reason: both proxy and real subject implement the same contract so clients remain ignorant.  
2. Implement the heavy/real object (`HighResolutionImage`) — reason: the real implementation for expensive operations.  
3. Implement `ImageProxy` that implements `Image`, holds a reference to the real object, lazily creates it on first use, and adds access control, caching, or logging.  
4. Client uses the `Image` interface and can be injected with a proxy.  
Files: `Image.java`, `HighResolutionImage.java`, `ImageProxy.java`.

## Iterator
1. Define or reuse the iterator interfaces (`Iterator<T>` / `Iterable<T>`) — reason: standard traversal contract.  
2. Implement the concrete collection (`Playlist` / `ConcreteCollection`) and provide an `iterator()` method.  
3. Implement the iterator class (`PlaylistIterator`) with `hasNext()` and `next()` (and `remove()` if needed) that encapsulates traversal state.  
4. Client obtains an iterator and traverses without depending on the collection internals.  
Files: `Playlist.java`, `PlaylistIterator.java`.

## Factory
1. Decide the factory style (Simple Factory, Factory Method, or Abstract Factory) based on needs — reason: different patterns address different variability and extensibility requirements.  
2. Define the product interface first (`Notification`) — reason: clients program to the product abstraction.  
3. Implement concrete products (`EmailNotification`, `SMSNotification`).  
4. Implement the chosen factory:
    - Simple Factory: `NotificationFactory.create(type)` for centralized logic.  
    - Factory Method: abstract `NotificationCreator.createNotification()` with concrete creators.  
    - Abstract Factory: families of related products via factory interfaces.  
5. Client uses the factory/creator to obtain products, not concrete classes.  
Files: `Notification.java`, `EmailNotification.java`, `NotificationFactory.java` or `NotificationCreator.java`.

## Composite
1. Define the component interface first (`FileSystemItem` with methods like `size()`, `print()` or `open()`) — reason: lets clients treat leaves and composites uniformly.  
2. Implement leaf classes (`File`) that perform the operation directly.  
3. Implement composite classes (`Folder`) that maintain `List<FileSystemItem>` and implement operations by delegating/aggregating to children; add `add()/remove()` methods.  
4. Client builds trees of `FileSystemItem` and uses the common interface for operations.  
Files: `FileSystemItem.java`, `File.java`, `Folder.java`.

## Implementing Prototype
Step 1: Define the Prototype Interface with one clone method EnemyProtoType.
Step 2: Create the Concrete Prototype Class (Enemy). be mindful of shallow or deep copy?
Step 3 (Optional): Create a Prototype Registry (EnemyRegistry)
Step 4: Using the Registry in Your Game



Notes (apply to all patterns)
- Start with the interface/abstract contract that clients will use; this maximizes decoupling and makes testing/stubbing easy.  
- Implement concrete classes next and keep construction/creation responsibilities centralized (builders/factories) when object creation is nontrivial.  
- Add unit tests for both contracts and concrete behaviors and small example code showing client usage.  
- Name files and classes consistently and keep responsibilities single-focused.