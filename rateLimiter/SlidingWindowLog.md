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

* Hinglish
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

