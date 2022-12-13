package twitter;

public interface Follower {

    void addToTimeline(Tweet tweet);

    void follow(Followed followed);

}
