# Consistent Hashing in Java — Industry‑grade Implementation

Below is a small, production‑style library you can drop into interview demos or real code. It includes:

* A generic **consistent hash ring** with **virtual nodes** and **weights**
* Pluggable **hash function** (with a fast, dependency‑free **Murmur3 32‑bit** implementation)
* **Thread‑safe** operations using `ConcurrentSkipListMap` + `ReadWriteLock`
* APIs for **single owner** (primary) and **N owners** (for replication)
* Clear separation of concerns + Javadoc
* A runnable **demo** and a thorough **README**

---

## 📂 Project Structure

```
consistent-hashing/
├── src/main/java/
│   └── com/example/ch/
│       ├── HashFunction.java
│       ├── Murmur3_32.java
│       ├── HashRingNode.java
│       ├── ConsistentHashRing.java
│       └── Demo.java
└── README.md
```

---

## `HashFunction.java`

```java
package com.example.ch;

/**
 * Abstraction for hashing strings into a 32-bit, non-negative integer space.
 * A pluggable interface lets you switch to stronger/faster hashes without
 * touching the ring logic.
 */
public interface HashFunction {
    /** Returns a non-negative 32-bit hash for the input. */
    int hash32(String input);
}
```

---

## `Murmur3_32.java`

```java
package com.example.ch;

/**
 * Minimal, dependency-free Murmur3 32-bit (x86) hash implementation.
 * Produces non-negative int by masking sign bit.
 *
 * Murmur3 is widely used in distributed systems for good avalanche behavior
 * and speed compared to MD5/SHA.* for table lookups.
 */
public final class Murmur3_32 implements HashFunction {
    private static final int SEED = 0x9747b28c; // arbitrary constant

    @Override
    public int hash32(String input) {
        byte[] data = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int length = data.length;
        int h1 = SEED;
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;

        int roundedEnd = (length & 0xfffffffc); // round down to 4 byte block
        for (int i = 0; i < roundedEnd; i += 4) {
            int k1 = ((data[i] & 0xff)) |
                     ((data[i + 1] & 0xff) << 8) |
                     ((data[i + 2] & 0xff) << 16) |
                     ((data[i + 3] & 0xff) << 24);
            k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
        }

        int k1 = 0;
        int tail = length & 0x03;
        if (tail == 3) {
            k1 ^= (data[roundedEnd + 2] & 0xff) << 16;
        }
        if (tail >= 2) {
            k1 ^= (data[roundedEnd + 1] & 0xff) << 8;
        }
        if (tail >= 1) {
            k1 ^= (data[roundedEnd] & 0xff);
            k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2;
            h1 ^= k1;
        }

        h1 ^= length;
        // fmix
        h1 ^= (h1 >>> 16);
        h1 *= 0x85ebca6b;
        h1 ^= (h1 >>> 13);
        h1 *= 0xc2b2ae35;
        h1 ^= (h1 >>> 16);

        return h1 & 0x7fffffff; // ensure non-negative
    }
}
```

---

## `HashRingNode.java`

```java
package com.example.ch;

import java.util.Objects;

/**
 * Represents a logical server/partition on the ring.
 * Immutable identity; weight controls number of virtual nodes.
 */
public final class HashRingNode {
    private final String id;     // e.g., "redis-10.0.0.5:6379" or "broker-3"
    private final int weight;    // relative capacity; >= 1

    public HashRingNode(String id) { this(id, 1); }

    public HashRingNode(String id, int weight) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id required");
        if (weight < 1) throw new IllegalArgumentException("weight must be >= 1");
        this.id = id;
        this.weight = weight;
    }

    public String id() { return id; }
    public int weight() { return weight; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashRingNode)) return false;
        HashRingNode that = (HashRingNode) o;
        return id.equals(that.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }
    @Override public String toString() { return id + "(w=" + weight + ")"; }
}
```

---

## `ConsistentHashRing.java`

```java
package com.example.ch;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe consistent hash ring with virtual nodes and weighted placement.
 *
 * Data structures chosen:
 * - ConcurrentSkipListMap<Integer, HashRingNode> for the ring: sorted, lock-free reads,
 *   O(log N) ceiling/firstKey/tailMap; safe under concurrent access.
 * - ReadWriteLock to coordinate structural mutations (add/remove nodes) with readers
 *   while keeping reads mostly lock-free.
 */
public class ConsistentHashRing {
    private final HashFunction hashFn;
    private final int replicasPerWeight; // base replicas per unit weight

    // key = position (hash on ring), value = owning node
    private final ConcurrentSkipListMap<Integer, HashRingNode> ring = new ConcurrentSkipListMap<>();

    // reverse index to remove a node's virtual positions efficiently
    private final Map<HashRingNode, List<Integer>> nodeToPositions = new HashMap<>();

    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    public ConsistentHashRing(HashFunction hashFn, int replicasPerWeight) {
        if (replicasPerWeight < 1) throw new IllegalArgumentException("replicasPerWeight >= 1");
        this.hashFn = Objects.requireNonNull(hashFn);
        this.replicasPerWeight = replicasPerWeight;
    }

    /** Add a node with V = weight * replicasPerWeight virtual nodes. */
    public void addNode(HashRingNode node) {
        rw.writeLock().lock();
        try {
            int vnodes = node.weight() * replicasPerWeight;
            List<Integer> positions = new ArrayList<>(vnodes);
            for (int i = 0; i < vnodes; i++) {
                int pos = hashFn.hash32(node.id() + "#" + i);
                // collision extremely unlikely, but if happens, shift slightly
                while (ring.containsKey(pos)) {
                    pos = hashFn.hash32(node.id() + "#" + i + ":" + pos);
                }
                ring.put(pos, node);
                positions.add(pos);
            }
            nodeToPositions.put(node, positions);
        } finally {
            rw.writeLock().unlock();
        }
    }

    /** Remove node and all its virtual positions. */
    public void removeNode(HashRingNode node) {
        rw.writeLock().lock();
        try {
            List<Integer> positions = nodeToPositions.remove(node);
            if (positions != null) {
                for (Integer p : positions) ring.remove(p);
            }
        } finally {
            rw.writeLock().unlock();
        }
    }

    /** Returns the primary owner for a given key (or null if ring empty). */
    public HashRingNode locate(String key) {
        rw.readLock().lock();
        try {
            if (ring.isEmpty()) return null;
            int h = hashFn.hash32(key);
            Map.Entry<Integer, HashRingNode> e = ring.ceilingEntry(h);
            if (e == null) e = ring.firstEntry();
            return e.getValue();
        } finally {
            rw.readLock().unlock();
        }
    }

    /** Returns the first k distinct owners clockwise (for replication). */
    public List<HashRingNode> locateN(String key, int k) {
        if (k <= 0) return Collections.emptyList();
        rw.readLock().lock();
        try {
            if (ring.isEmpty()) return Collections.emptyList();
            int h = hashFn.hash32(key);
            NavigableMap<Integer, HashRingNode> tail = ring.tailMap(h, true);
            List<HashRingNode> owners = new ArrayList<>(k);
            addDistinct(owners, tail.values().iterator(), k);
            if (owners.size() < k) {
                addDistinct(owners, ring.values().iterator(), k);
            }
            return owners;
        } finally {
            rw.readLock().unlock();
        }
    }

    private static void addDistinct(List<HashRingNode> out, Iterator<HashRingNode> it, int k) {
        while (it.hasNext() && out.size() < k) {
            HashRingNode n = it.next();
            if (!out.contains(n)) out.add(n);
        }
    }

    /** Current ring size in virtual nodes (debug/metrics). */
    public int size() {
        rw.readLock().lock();
        try { return ring.size(); }
        finally { rw.readLock().unlock(); }
    }
}
```

---

## `Demo.java`

```java
package com.example.ch;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Hash
```
