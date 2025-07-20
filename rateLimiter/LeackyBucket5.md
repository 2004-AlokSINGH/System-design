🔁 Quick Recap of Older Approaches:
```
| Algorithm                  | Problem It Solves             | Weakness                             |
| -------------------------- | ----------------------------- | ------------------------------------ |
| Fixed Window Counter       | Easy to implement burst limit | Allows sudden burst at window edge   |
| Sliding Window Log         | Precise burst tracking        | Memory-heavy (stores all timestamps) |
| Sliding Window Counter     | Approximates smoother rate    | Still allows short bursts            |

```
 Need – Why Leaky Bucket?
🔥 Problem in Real Systems:
APIs often receive spikes of requests (burst traffic)

Even if average request rate is acceptable, sudden bursts can:
- Crash systems
- Overload servers
- Cause unfair usage


🧠 Core Idea
- Imagine a bucket with a small hole.
- Requests are like water poured into the bucket.
- Water leaks out at a fixed rate.
- If the bucket overflows, new requests are dropped (rejected).


💧 What Leaky Bucket Solves (That Others Don’t)
```
| Issue in Other Algos             | How Leaky Bucket Fixes It                    |
| -------------------------------- | -------------------------------------------- |
| ❗ **Burst requests allowed**     | ❌ Drops excess requests — no bursting        |
| ⚠️ **Uneven load processing**    | ✅ Processes at **constant rate** (leak rate) |
| 💣 **Traffic spikes = overload** | ✅ Smooths all traffic, avoids overload       |
| 🧠 **Memory grows (in log)**     | ✅ Uses fixed-size counters — no log storage  |

```



**Leaky Bucket enforces strict, steady processing even if traffic is spiky.**

🔍 Where Other Algos Fail
Imagine:
- API allows 10 requests per 10 seconds (limit = 10)
- Client sends 10 requests in 1 second, then nothing.

Algorithm	Result 
- Fixed / Sliding Counter	All 10 requests pass ✅
- Leaky Bucket (rate = 1/sec)	Only ~1–2 pass ✅❌❌❌...

✅ Why?
Because Leaky Bucket says: 
- **“I don’t care how fast you pour water — I only leak at 1 drop per second.”**

✅ Summary – Why Leaky Bucket Is Unique
```
Concept	Leaky Bucket
Smooth traffic	               ✅ Yes (strict)
Drops bursts	                 ✅ Immediately if rate exceeded
Processing at constant pace	  ✅ Always
Old algo weakness solved	     ✅ Yes – burst handling + overload safety
```


```java
public class LeakyBucketRateLimiter {
    private final int capacity; // Max bucket size (how many requests it can hold)
    private final int leakRate; // How many requests per second are allowed to leak
    private int water = 0;      // Current amount of water (queued requests)
    private long lastLeakTime;

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastLeakTime;

        // Leak out water (process requests) at fixed rate
        int leaked = (int) (elapsed * leakRate / 1000);
        if (leaked > 0) {
            water = Math.max(0, water - leaked);
            lastLeakTime = now;
        }

        // Check if bucket can hold more water (queue room)
        if (water < capacity) {
            water++;  // Add new request (as water)
            return true;
        }

        return false; // Bucket full → reject request
    }

    // Test the limiter
    public static void main(String[] args) throws InterruptedException {
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(3, 1); // 3 capacity, 1 request/sec

        for (int i = 1; i <= 6; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));
            Thread.sleep(300); // Every 0.3 seconds
        }
    }
}

```

Why the earlier code is basic?
That Leaky Bucket code looked like this:

java
Copy
Edit
if (now - lastRequestTime < leakInterval) {
    return false; // reject
}
lastRequestTime = now;
return true; // allow
It just checks if minimum time has passed between two requests, i.e., strict fixed rate, no queuing.

❌ Problems with that:
❌ Rejects requests instantly even if they're “almost” okay

❌ Doesn't allow bursting or smoothing

❌ Doesn’t store any request queue

❌ No control over number of requests in system

✅ Better Version: Leaky Bucket as Queue-Based Traffic Shaper
Core idea:
Add a queue to simulate bucket

Process 1 request every leakInterval

Keep capacity limited

✅ Java Code: Queue-based Leaky Bucket

```java
import java.util.LinkedList;
import java.util.Queue;

public class LeakyBucketQueueRateLimiter {
    private final int capacity;
    private final long leakIntervalMillis;
    private final Queue<Long> bucket;
    private long lastLeakTime;

    public LeakyBucketQueueRateLimiter(int capacity, long leakIntervalMillis) {
        this.capacity = capacity;
        this.leakIntervalMillis = leakIntervalMillis;
        this.bucket = new LinkedList<>();
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        // Leak out one request per interval
        long leaked = (now - lastLeakTime) / leakIntervalMillis;
        for (int i = 0; i < leaked && !bucket.isEmpty(); i++) {
            bucket.poll(); // remove oldest
        }

        if (leaked > 0) {
            lastLeakTime += leaked * leakIntervalMillis;
        }

        // Add new request if bucket has space
        if (bucket.size() < capacity) {
            bucket.offer(now);
            return true; // allowed
        }

        return false; // rejected
    }

    public static void main(String[] args) throws InterruptedException {
        LeakyBucketQueueRateLimiter limiter = new LeakyBucketQueueRateLimiter(3, 1000);

        for (int i = 1; i <= 6; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "✅ Allowed" : "❌ Rejected"));
            Thread.sleep(300); // simulate burst
        }
    }
}


```


🔍 Code Snippet (Focus Section):
```java
// Add new request if bucket has space
if (bucket.size() < capacity) {
    bucket.offer(now);  // 👈 This is where the request is added to the queue
    return true;        // ✅ Allowed
}
```
🔑 What’s happening?
bucket is a Queue<Long> that stores timestamps of requests.

.offer(now) adds the current request's timestamp (now) into the queue.

But only if the queue has space (bucket.size() < capacity) — otherwise it's rejected.

🧠 Why do we store timestamps?
We don’t care about request data in rate limiting — we only care when they came. So we just add a timestamp (now) to mark the request’s arrival.

Later, as time passes, the leak logic removes old ones — simulating that they’ve been processed.

💧 Leak happens here:
``` java

long leaked = (now - lastLeakTime) / leakIntervalMillis;
for (int i = 0; i < leaked && !bucket.isEmpty(); i++) {
    bucket.poll();  // 👈 remove oldest request (simulate leak)
}
```
🧭 So to summarize:
- Request added to queue: ✅ at bucket.offer(now);
- Condition: only if space is available.
- Queue simulates bucket holding requests.
- Leaking (via .poll()) happens at constant rate, freeing space for new ones.


🚨 Problem with Leaky Bucket (Even with Queue):
Even after adding a queue, Leaky Bucket has a critical limitation:

❗️Main Issue: No Burst Tolerance (Rigid leak rate) 
- It leaks at a fixed rate, e.g., 1 request per second
- If 5 requests come suddenly (burst), only the first few fit the bucket
- Even if system is idle before, it won’t allow more than 1/sec
- It doesn’t “accumulate credits” — unused time is wasted

