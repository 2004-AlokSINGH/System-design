# 🧮 Fixed Window Rate Limiter

### 📌 Logic:
- Track number of requests per user per **fixed interval** (e.g., per minute).
- Reset counter when time moves to **next window**.

---

### Pros:
- ✅ Very simple to implement  
- ⚡ Fast `O(1)` access

###  Cons:
- ❗ Bursty traffic at window edges  
  e.g., `10 requests in last 1ms of window + 10 in first 1ms of next = 20 in 2ms`  
- ❌ Not suitable for precise control

---

### 💻 Java Code:

```java
class FixedWindowRateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private int requestCount = 0;
    private long windowStart = System.currentTimeMillis();

    public FixedWindowRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        if (now - windowStart >= windowSizeMillis) {
            requestCount = 0;
            windowStart = now;
        }
        if (requestCount < limit) {
            requestCount++;
            return true;
        }
        return false;
    }
}
```
If you use this in a REST API endpoint:

```java
FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(10, 1000);
```
Then all requests — no matter which user/IP — share this limiter. So:

10 total requests are allowed in the current 1-second window

The 11th is blocked — doesn't matter who sent it

Now to use this Rate limiter
```java
public class RateLimiterManager {
    private final Map<String, FixedWindowRateLimiter> limiters = new ConcurrentHashMap<>();

    public boolean allow(String userId) {
        limiters.putIfAbsent(userId, new FixedWindowRateLimiter(10, 1000));
        return limiters.get(userId).allowRequest();
    }
}
```

##### Q1. Why is the method synchronized?"
✅ Reason:
Java is multi-threaded — especially in web servers (Spring Boot, Tomcat, etc.). If multiple threads call allowRequest() at the same time, you can get:

Race conditions (requestCount++ is not atomic!)

Incorrect time checks

Double-counted or missed requests

🧠 Fix:
synchronized ensures:
Only one thread at a time can execute the method
Safe update of shared state: requestCount, windowStart

🔁 This is simple, but can hurt performance under high concurrency (better to use AtomicInteger, ReentrantLock, or LongAdder if needed later)


#### Q2. Why ConcurrentHashMap?
It’s a thread-safe implementation of Map:
Internally splits the map into segments/buckets
Allows multiple threads to access different keys concurrently
Operations like putIfAbsent(), get(), computeIfAbsent() are atomic
Performance is much better than using Collections.synchronizedMap() or making whole method synchronized

#### Q3. Why HashMap is Unsafe
HashMap is not thread-safe:
If 2 threads access the map simultaneously:
One inserts while another reads → race condition
Internal structure (buckets) can become corrupt
Can lead to ConcurrentModificationException
Or even infinite loops in earlier JDKs during rehashing (very dangerous)
