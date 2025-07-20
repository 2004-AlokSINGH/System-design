⚠️ Problem with Sliding Log Algorithm
Let’s summarize its main weakness:

Issue	Explanation
```
| Issue              | Explanation                                                        |
| ------------------ | ------------------------------------------------------------------ |
| **Memory**         | Stores **each request timestamp** → high memory if traffic is high |
| **Processing**     | On each request, it loops to remove old entries (O(n))             |
| **Bursty Traffic** | You can get bursts near window edges (e.g., 6 requests in 0.1s)    |
```


We want to reduce memory + CPU usage and smooth out traffic.

✅ Sliding Window Counter — Core Idea
Instead of tracking each request, group requests into time buckets.

📦 How?
Divide the total window (e.g., 10s) into smaller buckets (e.g., 10 buckets of 1s each)

When a request comes in:

Determine which bucket it falls into

Increment the counter for that bucket

To check if a request should be allowed:

Sum all counts in the sliding window

If total < limit → allow the request

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
Every second, the oldest bucket gets discarded

This mimics a "sliding" time window without storing timestamps

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

Faster than log method

Memory-bound (doesn’t grow with traffic)

Smoother control over request spikes

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
If you need precise timing-based rejection (e.g., payment systems)

If time window is too small (1s), then buckets may become inaccurate


