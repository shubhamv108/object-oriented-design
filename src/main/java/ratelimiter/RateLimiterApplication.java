package ratelimiter;

public class RateLimiterApplication {

    public static void main(String[] args) throws InterruptedException {
        int requestPerMinute = 10;
        RateLimiterFactory factory = new FixedWindowRateLimiterFactory();
        RateLimiter rateLimiter = factory.create(requestPerMinute);

        for (int i = 1; i < 15; ++i) {
            if (rateLimiter.allow())
                System.out.println(i + ": Allowed");
            else {
                System.out.println(i + ": Throttled");
//                Thread.sleep(60000);
            }
        }
    }

}
