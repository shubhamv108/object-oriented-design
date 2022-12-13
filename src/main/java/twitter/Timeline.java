package twitter;

import java.util.LinkedList;

public class Timeline {

    private final LinkedList<Tweet> tweets;

    private static final long MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 100;

    public Timeline() {
        this.tweets = new LinkedList<>();
    }

    public Timeline(LinkedList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public void add(Tweet tweet) {
        this.clearOld();
        this.tweets.offer(tweet);
    }

    private void clearOld() {
        while (!this.tweets.isEmpty() && System.currentTimeMillis() - MILLISECONDS_IN_A_DAY > this.tweets.peek().getCreatedAt().getTime())
            this.tweets.poll();
    }

    public void sort(SortOrder order) {
        this.tweets.sort(TweetSorterFactory.get(order));
    }

    protected Timeline copy() {
        this.clearOld();
        return new Timeline(new LinkedList<>(this.tweets));
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "tweets=" + tweets +
                '}';
    }
}
