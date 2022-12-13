package twitter;

public interface Followed {

    void notifyFollowers(Tweet tweet);

    void addFollower(Follower follower);

}
