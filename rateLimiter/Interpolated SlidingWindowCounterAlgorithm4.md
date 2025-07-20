Evloution
```
| Level | Algorithm                          | Notes                                        |
| ----- | ---------------------------------- | -------------------------------------------- |
| 1     | **Fixed Window Counter**           | Simplest, inaccurate at boundaries           |
| 2     | **Sliding Log**                    | Accurate but memory-heavy                    |
| 3     | **Sliding Counter (Buckets)**      | Better memory, fixed window granularity      |
| 4     | **Interpolated Sliding Counter** ✅ | Smooth, fast, no growth — ideal balance      |
| 5     | **Leaky Bucket**                   | Smoothed output, constant drain              |
| 6     | **Token Bucket** ✅                 | Supports average + burst, best for real APIs |

```
<img width="1099" height="534" alt="image" src="https://github.com/user-attachments/assets/3a8ee939-dc5e-4c2b-a91d-537c4f8f339e" />


🚫 Problem It Solves
Problem in Fixed Window Counter:
Issue	Explanation
```
Hard cutoffs	Requests just after a new window starts all get allowed, causing burst
No smoothing	All requests from last window are suddenly forgotten
Imprecise under bursty traffic	Doesn’t fairly account for traffic near boundary
```

✅ How It Solves That
```
| Feature                 | How it Helps                                      |
| ----------------------- | ------------------------------------------------- |
| **Smoother transition** | Interpolates between current and previous windows |
| **No memory growth**    | Only 2 counters needed — fixed memory             |
| **Constant time**       | O(1) processing per request                       |
| **Good for APIs**       | Balances speed and fairness for high-QPS systems  |
```

📦 Basic Setup:
Setting	Value
Limit	10 requests per 60 sec
Current Time	t = 70 sec
Window Size	60 sec

🧠 We divide time into fixed windows:

Window 1: [0–60)

Window 2: [60–120)

Current time t = 70 falls in Window 2

🧠 Sliding Window Counter Idea:
We don’t store all timestamps. Instead, we keep:

Window	Request Count
Previous window	prevCount = 8
Current window	currCount = 3

📏 Weighting Logic:
Since current time = 70 sec → we’re 10 sec into current 60 sec window

➡️ So 10/60 = 0.166... (i.e., 16.7%) of current window has passed
We now interpolate:

🔢 Total Weighted Count:
```
effectiveCount = (1 - 0.1667) * prevCount + 0.1667 * currCount
               ≈ 0.833 * 8 + 0.167 * 3
               ≈ 6.66 + 0.5 ≈ 7.16
👉 Since 7.16 < 10 → ✅ Allow request

If it was > 10 → ❌ Reject request
```

```java

public class SlidingWindowCounterRateLimiter {
    private final int limit;
    private final long windowSizeMillis;

    // Track previous and current window start times and counts
    private long currentWindowStart;
    private int currentWindowCount;
    private int previousWindowCount;

    public SlidingWindowCounterRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.currentWindowStart = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        // Find how many full windows have passed
        long windowsPassed = (now - currentWindowStart) / windowSizeMillis;

        if (windowsPassed >= 1) {
            // Shift windows
            previousWindowCount = (windowsPassed == 1) ? currentWindowCount : 0;
            currentWindowCount = 0;

            // Move currentWindowStart to current window
            currentWindowStart += windowsPassed * windowSizeMillis;
        }

        // Weight = how much of the current window has passed (0 to 1)
        double weight = (now - currentWindowStart) / (double) windowSizeMillis;

        // Weighted total = blend of previous and current window
        double totalRequests = (1 - weight) * previousWindowCount + currentWindowCount;

        if (totalRequests < limit) {
            currentWindowCount++;
            return true;
        }

        return false;
    }

    // Sample usage
    public static void main(String[] args) throws InterruptedException {
        // Allow 5 requests per 10 seconds
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 10_000);

        for (int i = 1; i <= 10; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));

            // Simulate random intervals (some fast, some slow)
            Thread.sleep(i % 2 == 0 ? 1000 : 2000);
        }
    }
}

```
