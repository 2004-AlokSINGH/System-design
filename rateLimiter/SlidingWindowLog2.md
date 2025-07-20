### 🚨 Problem with Fixed Window:
It allows bursty traffic at window edges:

e.g. 10 requests at 00:59:999 + 10 at 01:00:001 → effectively 20 in 2 ms.


#### ✅ Sliding Window Log: How It Solves That
📌 Idea:
Instead of grouping requests in fixed intervals, maintain a log of timestamps of each request in a sliding window (e.g., last 60 seconds).

🔁 At each request:
- Remove timestamps older than **currentTime - windowSize**
- If remaining count < limit → allow request
- Else → reject

Problem with Fixed Window:
Fixed window counts requests in batches (e.g., 00:00 to 00:59). If you send 10 requests at the end of one window and 10 more at the start of the next, you're still technically under limit — but you just sent 20 requests in 2 ms, which can overload a server.

Sliding Window Log Fixes That:

It tracks exact timestamps of every request.

1. Each time a new request comes in:
  
2. Remove old requests older than (now - window size)
  
3. Count how many are still within the window
  
4. If count < limit → allow the new request
  
5. Else → reject it

Why It Works:

It prevents bursts at boundaries by always keeping a real-time view of the last X seconds.

Requests are evenly spread out because the limiter checks "sliding" windows, not fixed chunks.

** Hinglish**

* Tum har request ka exact time store karte ho.
* Har nayi request ke time pe:
* Tum check karte ho ki last 60 seconds me kitni request already aayi hain
* Agar limit (10) se kam hai → allow
* Agar limit cross ho gaya → reject

 Minus karke kya fayda? **currentTime - windowSize**
Jab bhi request aaye, tum:
* Check karte ho last X second me kaun-kaun si request valid hai (jo window ke andar aati hai)
* Jo bahar ho gayi (old ho gayi) unko hata do
* Toh tumhara rate check sliding way me hota hai, continuous window ke basis pe — not rigid like fixed window


<img width="820" height="428" alt="image" src="https://github.com/user-attachments/assets/901417fe-0f19-450d-8518-d2f4c7e22ec2" />



🧠 Data Structure Used:
Queue / LinkedList (or Deque) to store timestamps

We need:

Fast insertion at end (O(1))

Fast removal from front (O(1))

Count of elements in the current window

📌 So we use:
➡️ Queue<Long> (e.g., LinkedList in Java)

**Implementation**

```java
import java.util.Deque;
import java.util.LinkedList;

/**
 * Sliding Window Log-based Rate Limiter
 * Limits the number of requests allowed in a sliding time window
 */
public class SlidingWindowLogRateLimiter {
    private final int limit;  // Max number of requests allowed
    private final long windowSizeMillis;  // Time window in milliseconds
    private final Deque<Long> requestTimestamps = new LinkedList<>();  // Stores timestamps of allowed requests

    public SlidingWindowLogRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
    }

    /**
     * Checks if a new request is allowed at current time
     */
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        // Step 1: Remove timestamps older than (now - window)
        while (!requestTimestamps.isEmpty() && requestTimestamps.peekFirst() <= now - windowSizeMillis) {
            requestTimestamps.pollFirst();  // remove oldest
        }

        // Step 2: Allow if current request count < limit
        if (requestTimestamps.size() < limit) {
            requestTimestamps.offerLast(now);  // record current request time
            return true;  // allow
        }

        return false;  // reject
    }

    /**
     * Main method to test the rate limiter
     */
    public static void main(String[] args) throws InterruptedException {
        // Allow 3 requests per 10 seconds
        SlidingWindowLogRateLimiter rateLimiter = new SlidingWindowLogRateLimiter(3, 10_000);

        // Simulate 5 rapid requests
        for (int i = 1; i <= 5; i++) {
            boolean allowed = rateLimiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));

            // Wait 1 second between requests
            Thread.sleep(1000);
        }

        // Wait for window to expire
        System.out.println("⏳ Waiting 10 seconds to reset window...");
        Thread.sleep(10_000);

        // Make 2 more requests after window reset
        for (int i = 6; i <= 7; i++) {
            boolean allowed = rateLimiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));
        }
    }
}

```

```
| Step                | What Happens?                                    |
| ------------------- | ------------------------------------------------ |
| Request comes in    | Current timestamp is taken                       |
| Clean old entries   | Timestamps older than `now - window` are removed |
| Check size of queue | If less than limit → allow & store timestamp     |
| Else                | Reject request                                   |
```


⚙️ Time Complexity:
O(1) average for insert/remove (amortized)

Memory usage = proportional to number of requests per window

📈 Pros:
✅ Smooth rate limiting

✅ No edge bursts

❌ Cons:
🧠 Slightly more complex than fixed window

🧠 Memory grows with number of requests per user




