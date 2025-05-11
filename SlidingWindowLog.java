package implementations.java.rate_limiting;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple Sliding Window Log rate limiter.
 */
public class SlidingWindowLog {

    // ✅ The size of the time window (in seconds) to enforce limits.
    private final long windowSizeInSeconds;

    // ✅ Maximum number of allowed requests in the current window.
    private final long maxRequestsPerWindow;

    // ✅ Log (queue) to keep timestamps of incoming requests.
    private final Queue<Long> requestLog;

    /**
     * Constructor to initialize the window size and limit.
     */
    public SlidingWindowLog(long windowSizeInSeconds, long maxRequestsPerWindow) {
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.requestLog = new LinkedList<>();
    }

    /**
     * Checks if a new request is allowed under the rate limiting policy.
     * @return true if allowed, false if rejected.
     */
    public synchronized boolean allowRequest() {
        long now = Instant.now().getEpochSecond(); // Current timestamp in seconds
        long windowStart = now - windowSizeInSeconds;

        // ✅ Clean old timestamps from the queue
        while (!requestLog.isEmpty() && requestLog.peek() <= windowStart) {
            requestLog.poll(); // Remove timestamps outside the current window
        }

        // ✅ If under the limit, allow and log the request
        if (requestLog.size() < maxRequestsPerWindow) {
            requestLog.offer(now);  // Log this request's timestamp
            return true;            // Allow the request
        }

        // ❌ Limit exceeded
        return false;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SlidingWindowLog rateLimiter = new SlidingWindowLog(10, 3); // 3 requests per 10 seconds

        for (int i = 0; i < 5; i++) {
            boolean allowed = rateLimiter.allowRequest();
            System.out.println("Request " + (i + 1) + ": " + (allowed ? "Allowed" : "Blocked"));
            Thread.sleep(2000); // wait 2 seconds between requests
        }
    }
}


⚠️ Limitations
Memory consumption increases with traffic because each request's timestamp is stored.
Not suitable for very high traffic systems — in such cases, prefer Sliding Window Counter or Token Bucket algorithm.
