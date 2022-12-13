package twitter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class User implements ITwitterUser, Follower, Followed {

    private final Account account;

    private final Set<Follower> followers = new HashSet<>();

    private final LinkedList<Tweet> tweets = new LinkedList<>();

    private final Timeline timeline = new Timeline();

    public User(final Account account) {
        this.account = account;
    }

    @Override
    public void notifyFollowers(Tweet tweet) {
        this.followers.forEach(follower -> follower.addToTimeline(tweet));
    }

    @Override
    public void addFollower(Follower follower) {
        this.followers.add(follower);
    }

    @Override
    public void addToTimeline(Tweet tweet) {
        this.timeline.add(tweet);
    }

    @Override
    public void follow(Followed followed) {
        followed.addFollower(this);
    }

    @Override
    public Tweet tweet(String content) {
        Tweet tweet = new Tweet(content, this);
        this.tweets.offerFirst(tweet);
        this.notifyFollowers(tweet);
        return tweet;
    }

    @Override
    public Timeline getHomeTimeline() {
       return new Timeline(new LinkedList<>(this.tweets));
    }

    @Override
    public Timeline getTimeLine() {
        return this.timeline.copy();
    }

    @Override
    public void followUser(ITwitterUser user) {
        this.follow((User) user);
    }

    @Override
    public void comment(String content, Commentable commentable) {
        commentable.addComment(content, this);
    }

    @Override
    public void like(Likable likable) {
        likable.like(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return account.getUserName().equals(user.account.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(account.getUserName());
    }

    @Override
    public String toString() {
        return account.getUserName();
    }
}
