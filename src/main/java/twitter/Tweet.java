package twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tweet implements Commentable, Likable {
    private final String content;

    private final List<Comment> comments = new ArrayList<>();

    private final Set<ITwitterUser> likedBy = new HashSet<>();

    private final ITwitterUser tweetedBy;

    private final Date createdAt;

    public Tweet(String content, ITwitterUser tweetedBy) {
        this.content = content;
        this.tweetedBy = tweetedBy;
        this.createdAt = new Date();
    }

    @Override
    public void addComment(String content, ITwitterUser commentedBy) {
        Comment comment = new Comment(commentedBy, content, this);
        this.comments.add(comment);
    }

    @Override
    public void like(ITwitterUser likedBy) {
        this.likedBy.add(likedBy);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                ", content='" + content + '\'' +
                ", comments=" + comments +
                ", likedBy=" + likedBy +
                ", tweetedBy=" + tweetedBy +
                ", createdAt=" + createdAt +
                '}';
    }

    public boolean isBefore(Tweet t2) {
        return this.createdAt.before(t2.createdAt);
    }
}
