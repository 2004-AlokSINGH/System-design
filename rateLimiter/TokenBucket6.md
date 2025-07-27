#### 🪣 Problem with Leaky Bucket (Example)
Scenario:
You are allowed 10 requests per second.

Requests are processed at a constant rate (1 request every 100ms).

```
| Time (ms) | Event                                       |
| --------- | ------------------------------------------- |
| 0         | You send a burst of **10 requests at once** |
| 100ms     | Leaky Bucket processes 1st request          |
| 200ms     | Processes 2nd request                       |
| ...       |                                             |
| 900ms     | Processes 9th request                       |
| 1000ms    | Processes 10th request                      |


```

Problem:
Even though you're allowed 10 requests/sec, Leaky Bucket enforces a fixed output rate.

You wanted to burst 10 requests instantly, but the bucket only "leaks" them out at a constant rate.

All burst requests are delayed → You experience high latency, even though your quota is not exceeded.

#### 🪙 Solution: Token Bucket (Same Example)
How Token Bucket Works:
You have a bucket that holds tokens.

Each token allows you to send 1 request.

Tokens are refilled at a constant rate.

If you have tokens, you can send requests immediately, allowing bursts up to the token count.

Example Case:
Token Bucket Capacity = 10 tokens.

Refill Rate = 10 tokens per second.

```

| Time (ms) | Event                                                                   |
| --------- | ----------------------------------------------------------------------- |
| 0         | Bucket is full (10 tokens)                                              |
| 0         | You send **10 requests at once** → Allowed immediately (tokens used up) |
| 100ms     | 1 token refilled                                                        |
| 150ms     | You send 1 more request → Allowed                                       |
| ...       |                                                                         |
| 1000ms    | Bucket refilled to full (10 tokens again)                               |

```

Advantage:
- Burst is allowed instantly if tokens are available.
- Over time, the refill rate enforces the average rate limit.
- No artificial delays for bursts.
- Idle periods accumulate tokens, allowing future bursts.

```
| Feature              | Leaky Bucket                    | Token Bucket                           |
| -------------------- | ------------------------------- | -------------------------------------- |
| Fixed Outflow Rate   | ✅ Yes (constant leak rate)      | ❌ No (can burst if tokens available)   |
| Burst Handling       | ❌ Bad (delayed even if allowed) | ✅ Good (instant if tokens are present) |
| Idle Time Efficiency | ❌ Tokens are wasted             | ✅ Tokens accumulate for future bursts  |
| Average Rate Control | ✅ Yes                           | ✅ Yes                                  |

```

🧠 How Token Bucket Solves Leaky Bucket’s Shortcomings
Allows bursts when traffic spikes and there are tokens available.

Uses idle time: tokens accumulate during idle periods, so a burst later can be accommodated.

Maintains long-term rate control via token refill rate → average throughput is still bounded


```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {
    private final int capacity;
    private final double refillRate; // tokens per second
    private double tokens;
    private long lastRefillTimestamp;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucket(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0;
        double tokensToAdd = elapsedSeconds * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }

    public boolean allowRequest() {
        lock.lock();
        try {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }
}


public class RateLimiterService {
    private final ConcurrentHashMap<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final double refillRate;

    public RateLimiterService(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    public boolean isRequestAllowed(String userId) {
        TokenBucket bucket = userBuckets.computeIfAbsent(userId, 
            id -> new TokenBucket(capacity, refillRate));
        return bucket.allowRequest();
    }
}



public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiterService rateLimiter = new RateLimiterService(5, 2); // 5 tokens max, 2 tokens/sec

        String userId = "user123";

        // Simulate a burst of requests
        for (int i = 0; i < 7; i++) {
            System.out.println("Request " + (i + 1) + ": " + 
                (rateLimiter.isRequestAllowed(userId) ? "Allowed" : "Rate Limited"));
            Thread.sleep(200);  // Simulate time between requests
        }

        // Wait to refill tokens
        System.out.println("Waiting 3 seconds to refill tokens...");
        Thread.sleep(3000);

        System.out.println("Request after refill: " + 
            (rateLimiter.isRequestAllowed(userId) ? "Allowed" : "Rate Limited"));
    }
}

```

🎯 Scenario:
A user hits your API endpoint. The system must allow or reject the request based on rate limits, considering distributed app servers.

🪜 Step-by-Step Flow Explanation
1️⃣ User Sends API Request
A client (browser, mobile app, service) sends an HTTP request to an API endpoint.

Example: GET /user/profile

2️⃣ API Gateway / CDN (Optional Edge Layer)
First point of contact.

Basic rate limits/DDoS protections can be enforced here (e.g., AWS API Gateway, Cloudflare).

If passed, forwards the request to backend load balancer.

3️⃣ Load Balancer Distributes Request
Load Balancer (ELB, NGINX, Envoy) picks an available App Server.

App servers are stateless microservices horizontally scaled.

4️⃣ App Server Receives Request
The App Server checks the userId / API Key from the request.

App Server does not maintain in-memory rate limits to avoid inconsistency (since there are multiple nodes).

It prepares a Redis query to check and update the token bucket.

5️⃣ App Server Calls Redis Lua Script (Token Bucket Logic)
Redis stores per-user token bucket state:

Key: rate_limit:{userId}

Fields: {tokens_remaining, last_refill_timestamp}

Lua script runs atomically in Redis:

Calculates tokens to refill based on current time.

Checks if requested tokens are available.

If yes → decrements token count and returns “allowed”.

If no → returns “rate limit exceeded”.

6️⃣ Redis Responds (Allow / Deny)
Response is fast (1-5ms).

App server receives:

✅ Allow → proceeds to handle request.

❌ Rate Limit Exceeded → responds with HTTP 429 (Too Many Requests).

7️⃣ Request Processing (If Allowed)
If allowed, the App Server processes business logic.

Queries DB or Cache (if needed).

Sends back a 200 OK response to the client.

8️⃣ Optional Persistent DB (For User Plans)
If rate limit configs (like Free/VIP user limits) are dynamic, App Servers may fetch per-user rate limits from a DB/config service.

These are cached to reduce DB hits.
