package twitter;

import java.util.Date;

public class Comment {

    private final ITwitterUser commentedBy;

    private final String content;

    private final Tweet tweet;

    private final Date createdAt;

    public Comment(ITwitterUser commentedBy, String content, Tweet tweet) {
        this.content = content;
        this.commentedBy = commentedBy;
        this.tweet = tweet;
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentedBy=" + commentedBy +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
