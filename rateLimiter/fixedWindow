# 🧮 Fixed Window Rate Limiter

### 📌 Logic:
- Track number of requests per user per **fixed interval** (e.g., per minute).
- Reset counter when time moves to **next window**.

---

### ✅ Pros:
- ✅ Very simple to implement  
- ⚡ Fast `O(1)` access

### ❌ Cons:
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
