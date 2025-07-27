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
# Distributed Rate Limiter - Real Life Flow Explanation

## Scenario:

A user sends API requests to a large-scale service (e.g., Google Maps API / Tower Research trading API). The system must ensure the user does not exceed their rate limit (e.g., 100 requests per second).

---

## Step-by-Step Flow:

### 1. API Request Hits API Gateway

* User sends an API request.
* API Gateway (e.g., Cloudflare, AWS API Gateway, NGINX) is the first entry point.
* **Optional:** Perform edge-level rate limiting to block obvious abuse (DDoS filtering).

### 2. Request Routed to Load Balancer

* API Gateway forwards allowed requests to a Load Balancer.
* Load Balancer distributes traffic across multiple App Server instances.

### 3. App Server Receives the Request

* The selected App Server node (stateless microservice) receives the request.
* Before processing, it must check if this request exceeds the user’s rate limit.

### 4. App Server Queries Redis (Token Bucket Check)

* App Server connects to a **Redis Cluster**.
* Executes a **Lua script** to:

  * Refill tokens based on time elapsed since last request.
  * Check if tokens are available.
  * Consume token if request is allowed.
  * All actions are done **atomically** within Redis.

### 5. Redis Returns Rate Limit Decision

* Redis returns result: **Allowed** or **Rate Limited**.
* This ensures all App Servers see the same token state (global consistency).

### 6. App Server Proceeds or Rejects

* If allowed: App Server processes the request (business logic).
* If rate-limited: Returns HTTP 429 (Too Many Requests) to the user.

### 7. Optional: Persistent DB for User Plan Config

* If rate limits vary per user (Free, Premium, VIP), App Servers can fetch plan configs from a **Persistent Database (RDBMS / NoSQL)**.
* These configs can be cached in Redis for faster lookups.

### 8. Token Bucket Refill Over Time

* Tokens for each user in Redis are refilled lazily on access, based on refill rate.
* Idle users accumulate tokens up to capacity, allowing bursts.

---

## Summary of Components:

| Component     | Role                                              |
| ------------- | ------------------------------------------------- |
| API Gateway   | Entry point; Optional DDoS & rate limiting        |
| Load Balancer | Distributes requests across App Servers           |
| App Server    | Stateless service; calls Redis for rate limiting  |
| Redis Cluster | Central token storage; ensures global consistency |
| Persistent DB | Stores user plan configs (optional)               |

---

## Advantages of This Flow:

* **Scalable**: Can handle millions of requests per second.
* **Accurate Global Limits**: Redis ensures consistency across all app nodes.
* **Low Latency (\~ms level)**: Lua scripts in Redis are fast and atomic.
* **Flexible Plans**: Different rate limits per user possible.
* **Burst Support**: Token Bucket allows controlled bursts after idle periods.

---

## Example User Flow:

1. User sends burst of 50 requests.
2. App Servers call Redis; 50 tokens are consumed if available.
3. If user’s plan allows (e.g., 100 req/sec), all 50 requests pass.
4. Subsequent requests within the same second may be rate-limited.
5. After 1 second, refill adds new tokens.

---

## Future Optimizations:

* **Local token warm-cache** for reducing Redis hits (approximate rate limits).
* **Sliding Window Counters** for smoother rate limiting.
* **Batch Token Consumption** for high throughput systems.
* **Distributed Redis Cluster with Sharding** for extreme scale.

---
