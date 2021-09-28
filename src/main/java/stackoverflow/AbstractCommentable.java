package stackoverflow;

import java.util.List;

public abstract class AbstractCommentable extends AbstractVotable {

    List<Comment> comments;

    public void addComment(Comment comment) {}
    public List<Comment> getComments() {
        return comments;
    }
}
