import java.time.Instant;

/**
 * FixedWindowCounter implements a basic fixed window rate limiter.
 */
public class FixedWindowCounter {

    private final long windowSizeInSeconds;   // The duration of each window (e.g., 60 seconds)
    private final long maxRequestsPerWindow;  // Maximum number of allowed requests in one window

    private long currentWindowStart;          // Start time of the current window (in epoch seconds)
    private long requestCount;                // Number of requests made in the current window

    /**
     * Constructor to initialize window size and max allowed requests.
     *
     * @param windowSizeInSeconds    The size of the fixed window in seconds.
     * @param maxRequestsPerWindow   The maximum number of requests allowed per window.
     */
    public FixedWindowCounter(long windowSizeInSeconds, long maxRequestsPerWindow) {
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.currentWindowStart = Instant.now().getEpochSecond(); // Set start time of current window
        this.requestCount = 0;
    }

    /**
     * Checks if a request is allowed under the current rate limiting window.
     *
     * @return true if the request is allowed, false if it's rate-limited.
     */
    public synchronized boolean allowRequest() {
        long now = Instant.now().getEpochSecond();

        // If we're in a new window, reset the count and move the window start
        if (now - currentWindowStart >= windowSizeInSeconds) {
            currentWindowStart = now;  // Start new window
            requestCount = 0;          // Reset request count
        }

        // Allow the request if we haven't hit the max yet
        if (requestCount < maxRequestsPerWindow) {
            requestCount++;  // Count this request
            return true;     // Allow the request
        }

        // Otherwise, deny the request
        return false;
    }
}

public class FixedWindowCounterTest {

    public static void main(String[] args) throws InterruptedException {
        // Allow 5 requests per 10 seconds
        FixedWindowCounter limiter = new FixedWindowCounter(10, 5);

        System.out.println("Sending 7 requests with 1 second interval...");

        for (int i = 1; i <= 7; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "BLOCKED"));
            Thread.sleep(1000); // Wait for 1 second between requests
        }

        System.out.println("Waiting 10 seconds to reset the window...");
        Thread.sleep(10000); // Wait for new window to begin

        System.out.println("Sending 2 more requests after window reset...");
        for (int i = 1; i <= 2; i++) {
            boolean allowed = limiter.allowRequest();
            System.out.println("Request after reset " + i + ": " + (allowed ? "ALLOWED" : "BLOCKED"));
        }
    }
}
