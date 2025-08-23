Consistent Hashing in Java

This document explains a simple Java implementation of Consistent Hashing, used in system design for load balancing and distributed systems.
---
📌 What is Consistent Hashing?

Consistent Hashing is a technique to distribute data across multiple servers/nodes in a way that:

Adding or removing a node does not require remapping all keys (only a small fraction move).

Nodes are placed on a logical ring (0–MAX_HASH).

Each key is assigned to the first node clockwise from its hash value.
---
```java
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {

  private final HashFunction hashFunction;
  private final int numberOfReplicas;
  private final SortedMap<Integer, T> circle = new TreeMap<>();

  public ConsistentHash(HashFunction hashFunction,
    int numberOfReplicas, Collection<T> nodes) {

    this.hashFunction = hashFunction;
    this.numberOfReplicas = numberOfReplicas;

    for (T node : nodes) {
      add(node);
    }
  }

  public void add(T node) {
    for (int i = 0; i < numberOfReplicas; i++) {
      circle.put(hashFunction.hash(node.toString() + i), node);
    }
  }

  public void remove(T node) {
    for (int i = 0; i < numberOfReplicas; i++) {
      circle.remove(hashFunction.hash(node.toString() + i));
    }
  }

  public T get(Object key) {
    if (circle.isEmpty()) {
      return null;
    }
    int hash = hashFunction.hash(key);
    if (!circle.containsKey(hash)) {
      SortedMap<Integer, T> tailMap = circle.tailMap(hash);
      hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  } 
}

```
🔍 Explanation of Variables

hashFunction:
Custom function to convert keys/strings into an integer hash value.
→ Why? We need uniform distribution of nodes/keys on the ring.

numberOfReplicas:
Each physical node is mapped multiple times on the ring.
→ Why? Prevents uneven distribution if hash space is skewed.
→ Each replica = a virtual node.

circle:
A SortedMap<Integer, T> implemented as a TreeMap.
→ Keys = hash values.
→ Values = nodes.
→ Why TreeMap?

Maintains sorted order.

Provides tailMap(hash) to quickly find the next clockwise node in O(log N).

🔧 Methods
add(T node)

Hash the node with suffixes 0..numberOfReplicas-1.

Insert into the ring (circle) at those hash positions.

✅ This ensures that each physical node is represented by multiple virtual nodes.

remove(T node)

Removes all virtual nodes belonging to that physical node.

✅ Useful when a node is decommissioned.

get(Object key)

Hash the key.

If hash exists in the circle → return mapped node.

Else → use tailMap(hash) to find the next clockwise node.

If tailMap is empty (wrap-around) → use firstKey().

✅ This guarantees every key has a node to map to.

⚙️ Why These Data Structures?

TreeMap (Red–Black Tree under the hood)

Keeps entries sorted by hash values.

tailMap() → efficiently find “next clockwise” node.

firstKey() → efficiently wrap-around at start of ring.

All operations in O(log N).

Virtual Nodes (numberOfReplicas)

Avoids skew where one node gets most keys.

Distributes keys more evenly across physical nodes.

---
🏭 Real-World Usage

Databases / Caches:
Cassandra, DynamoDB, Redis Cluster, Memcached all use consistent hashing.

Load Balancers:
Distribute requests across servers without heavy rebalancing.

Industry Twist:

Real systems use MD5/SHA-1 (not toy hash functions).

Some use HashRing libraries with weighted nodes (stronger servers get more virtual nodes).

Fault tolerance: nodes may replicate data to their next N neighbors on the ring.


---
🎯 Key Takeaways

TreeMap gives ordered access for fast node lookup.

numberOfReplicas smooths out load balancing.

Only a fraction of keys remap when nodes join/leave.

---
Industry systems add:

Better hashing (SHA/MD5).

Replication for fault tolerance.

Weighted virtual nodes.
