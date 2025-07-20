⚠️ Problem with Sliding Log Algorithm
Let’s summarize its main weakness:

Issue	Explanation
```
| Issue              | Explanation                                                        |
| ------------------ | ------------------------------------------------------------------ |
|   Memory           | Stores **each request timestamp** → high memory if traffic is high |
|   Processing       | On each request, it loops to remove old entries (O(n))             |
|  Bursty Traffic    | You can get bursts near window edges (e.g., 6 requests in 0.1s)    |
```


We want to reduce memory + CPU usage and smooth out traffic.

✅ Sliding Window Counter — Core Idea
Instead of tracking each request, group requests into time buckets.

📦 How?
Divide the total window (e.g., 10s) into smaller buckets (e.g., 10 buckets of 1s each)

### ✅ Sliding Window Counter Algorithm (Steps)

1. **When a request comes in:**
   - 1.1. Determine which time bucket the request falls into.
   - 1.2. Increment the counter for that bucket.

2. **To check if a request should be allowed:**
   - 2.1. Sum the counts of all buckets in the sliding window.
   - 2.2. If the total count is **less than the allowed limit**, then **allow the request**.
   - 2.3. Else, **reject the request**.


🧊 Visualization:
Let’s say we allow 3 requests per 10s, with 10 buckets (1s each)

```
| Time    | Bucket# | Count           |
| ------- | ------- | --------------- |
| t = 1s  | B1      | 1               |
| t = 2s  | B2      | 1               |
| t = 5s  | B5      | 1               |
| **Sum** |         | **3** ✅ Allowed |

```


🧃 Sliding Effect:
- Every second, the oldest bucket gets discarded
- This mimics a "sliding" time window without storing timestamps

Example:
At t = 12s, bucket at t = 1s is too old → dropped from sum
New request checks sum of buckets [t=2s → t=12s]

✅ How Does It Solve the Problem?
```
| Problem                      | Solution by Sliding Counter                                 |
| ---------------------------- | ----------------------------------------------------------- |
| High memory usage            | Only store N buckets (fixed memory)                         |
| CPU cost per request         | No loop through request log → just map read/write           |
| Burstiness near window edges | Since each second gets its own counter, bursts are averaged |
```


So:

- Faster than log method
- Memory-bound (doesn’t grow with traffic)
- Smoother control over request spikes

How it is better?
```
| Feature                | Sliding Log  | Sliding Counter      |
| ---------------------- | ------------ | -------------------- |
| Memory                 | O(requests)  | O(buckets) (fixed)   |
| CPU per request        | O(n) cleanup | O(1)                 |
| Bursty traffic control | Weak         | Stronger             |
| Accuracy               | High         | Medium (approximate) |
| Complexity             | Simple       | Slightly more logic  |
```


It’s a tradeoff: we give up a little accuracy (exact timestamps) for efficiency and scalability.

⛔ When Not Ideal?
- If you need precise timing-based rejection (e.g., payment systems)
- If time window is too small (1s), then buckets may become inaccurate

```java
import java.util.HashMap;
import java.util.Map;

public class SlidingWindowCounterRateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private final int bucketCount;
    private final long bucketSizeMillis;
    private final Map<Long, Integer> buckets = new HashMap<>();

    public SlidingWindowCounterRateLimiter(int limit, long windowSizeMillis, int bucketCount) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.bucketCount = bucketCount;
        this.bucketSizeMillis = windowSizeMillis / bucketCount;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        long currentBucket = now / bucketSizeMillis;

        // Clean up old buckets
        buckets.entrySet().removeIf(entry -> (now - (entry.getKey() * bucketSizeMillis)) >= windowSizeMillis);

        // Count requests in the current window
        int requestCount = 0;
        for (Map.Entry<Long, Integer> entry : buckets.entrySet()) {
            requestCount += entry.getValue();
        }

        if (requestCount < limit) {
            buckets.put(currentBucket, buckets.getOrDefault(currentBucket, 0) + 1);
            return true;
        }

        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(3, 10000, 10);

        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));
            Thread.sleep(1000);
        }

        System.out.println("⏳ Waiting 10 seconds...");
        Thread.sleep(10000);

        for (int i = 6; i <= 7; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));
        }
    }
}
```



