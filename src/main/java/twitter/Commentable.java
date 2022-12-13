package twitter;

public interface Commentable {

    void addComment(String content, ITwitterUser commentedBy);

}
