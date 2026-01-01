The Iterator Design Pattern is a **behavioral pattern** that provides a standard **way to access elements of a collection sequentially** without exposing its internal structure.

1. You need to traverse a collection (like a list, tree, or graph) in a consistent and flexible way.
2. You want to support multiple ways to iterate (e.g., forward, backward, filtering, or skipping elements).
3. You want to decouple traversal logic from collection structure, so the client doesn't depend on the internal representation.


```
When faced with this need, developers often write custom for loops or *expose the underlying data structures (like ArrayList or LinkedList) directly. 

For example, a Playlist class might expose its songs array and let the client iterate however it wants.

But this approach makes the client tightly coupled to the collection’s internal structure, and it violates encapsulation. If the internal storage changes, the client code breaks. It also becomes difficult to add new traversal logic or support lazy iteration

```

### The Iterator Pattern solves this by abstracting the iteration logic into a dedicated object — the iterator. 

This makes it easy to change the collection’s structure, add new traversal styles, and preserve clean, encapsulated designs.


#### 1. The Problem: Traversing a Playlist
Each playlist stores a list of songs and provides features like:

Playing songs one by one
Skipping to the next or previous song
Shuffling songs
Displaying the current song queue


```java
class Playlist {
    private List<String> songs = new ArrayList<>();

    public void addSong(String song) {
        songs.add(song);
    }

    public List<String> getSongs() {
        return songs;
    }
}

// client code

class MusicPlayer {
    public void playAll(Playlist playlist) {
        for (String song : playlist.getSongs()) {
            System.out.println("Playing: " + song);
        }
    }
}
```


Why This Is a Problem -
1. Breaks Encapsulation
By exposing the internal list of songs (getSongs()), you allow clients to directly modify the collection. This can lead to unintended side effects, like removing songs from the list, reordering them, or injecting nulls.

2. Tightly Couples Client to Collection Type
The client assumes that the playlist uses a List<String>. If you ever decide to change the internal storage (e.g., to a LinkedList, array, or even a streaming buffer), the client code breaks.

3. Limited Traversal Options
Supporting multiple traversal styles (e.g., reverse, shuffled, filtered) requires rewriting the loop logic every time — violating the Single Responsibility and Open/Closed principles.


#### What We Really Need
We need a clean, standardized way to:

Traverse songs in a playlist without exposing the internal collection
Allow different traversal styles (forward, reverse, shuffle)
Abstract the iteration logic from the data structure itself
Preserve encapsulation and allow the playlist to change internally without affecting client code.

Instead of letting clients access the underlying data, the collection exposes an iterator object that provides sequential access through a common interface. A clean way to organize this:

# Iterator Design Pattern
Key components
- Iterator (interface)
    - hasNext(): boolean — checks if more elements remain
    - next(): T — returns the next element
    - (optional) remove(): void or peek()/reset() depending on needs
- IterableCollection / Aggregate (interface)
    - createIterator(): Iterator<T> — returns an iterator for the collection

- ConcreteCollection (e.g., Playlist)
    - Implements the IterableCollection interface
    - Keeps internal storage private and returns concrete iterators
    - Provides limited accessors (e.g., size(), get(index)) to support iterators without exposing the full collection
- ConcreteIterator(s)
    - Hold a reference to the *ConcreteCollection* and traversal state (index, cursor, or an ordered list of indices)
    - Implement traversal behavior (ForwardIterator, ReverseIterator, ShuffleIterator, FilterIterator, etc.)
    - Encapsulate traversal logic so clients remain decoupled from internal representation

Notes
- Use different ConcreteIterator implementations to add traversal strategies without changing client code.
- Iterators can support lazy traversal and complex filters while preserving encapsulation of the aggregate.
- The aggregate may expose minimal, controlled accessors needed by iterators rather than the underlying container itself.

Minimal Java-like example:

```java
// Iterator interface
public interface Iterator<T> {
        boolean hasNext();
        T next();
}

// Aggregate / Iterable interface
public interface IterableCollection<T> {
        Iterator<T> createIterator();
}

// Concrete aggregate
public class Playlist implements IterableCollection<String> {
        private List<String> songs = new ArrayList<>();

        public void add(String song) { songs.add(song); }
        int size() { return songs.size(); }
        String get(int i) { return songs.get(i); }

        @Override
        public Iterator<String> createIterator() {
                return new PlaylistIterator(this);
        }
}

// Concrete iterator (forward)
public class PlaylistIterator implements Iterator<String> {
        private final Playlist playlist;
        private int index = 0;

        public PlaylistIterator(Playlist playlist) { this.playlist = playlist; }

        @Override
        public boolean hasNext() { return index < playlist.size(); }

        @Override
        public String next() { return playlist.get(index++); }
}
```

Notes:
- The iterator encapsulates traversal logic; the client uses only hasNext()/next(), preserving encapsulation.
- Add other concrete iterators (ReverseIterator, ShuffleIterator, FilterIterator) that also accept the same aggregate to provide different traversal behaviors without changing client code.
- The concrete iterator typically needs accessors on the aggregate (size/get) but not direct exposure of the underlying collection.


