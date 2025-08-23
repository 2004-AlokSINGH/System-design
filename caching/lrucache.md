# LRU Cache in Java

## 📖 What is an LRU Cache?
- **LRU (Least Recently Used)** is a caching strategy.
- When the cache is full, it removes the **least recently used item** to make space for a new one.

Example:
- Capacity = 2
- Put(1,10) → Cache = {1=10}
- Put(2,20) → Cache = {1=10, 2=20}
- Get(1) → Returns 10, mark 1 as recently used → Cache = {2,1}
- Put(3,30) → Evicts 2 (least used) → Cache = {1,3}


---

## 🛠️ Data Structures Used
1. **HashMap (key → Node)**  
   - Provides O(1) lookup for whether a key exists in the cache.
2. **Doubly Linked List**  
   - Maintains usage order (most recently used at head, least at tail).
   - O(1) insertion & deletion of nodes.

This combination ensures both `get()` and `put()` work in **O(1) time**.

---

## ⚡ Time & Space Complexity
- `get()` → **O(1)**
- `put()` → **O(1)**
- Space → **O(capacity)** for storing `HashMap` + `Doubly LinkedList`.

---

## 🔎 Why Not Just Use LinkedHashMap?
- Java’s `LinkedHashMap` can implement LRU with `removeEldestEntry`.  
- BUT: In interviews, using **HashMap + Doubly LinkedList** shows deeper understanding of system design & data structure tradeoffs.

---

## 🏗️ System Design Angle
- **Single Machine**: Works fine for small capacity (in-memory cache).
- **Scaling**:
  - **Sharding**: Divide cache across multiple servers.
  - **Eviction policies**: LRU vs LFU (Least Frequently Used).
  - **Persistence**: Add write-through or write-back strategies.
  - **Distributed Cache**: Use Redis or Memcached for real-world systems.

---



### Java implementation of an LRU Cache using the primary data structures (HashMap + Doubly LinkedList), which is the standard way interviewers expect.


```java
import java.util.HashMap;

class LRUCache {
    private class Node {
        int key, value;
        Node prev, next;
        Node(int k, int v) {
            key = k;
            value = v;
        }
    }

    private final int capacity;
    private final HashMap<Integer, Node> cache;
    private final Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        // Dummy head & tail for easy operations
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1; // not found
        }
        Node node = cache.get(key);
        moveToHead(node); // mark as recently used
        return node.value;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            if (cache.size() >= capacity) {
                Node lru = tail.prev;
                removeNode(lru);
                cache.remove(lru.key);
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
    }
}
```
